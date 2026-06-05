package com.cinepajeu.service.impl;

import com.cinepajeu.dto.VendaIngressoRequestDTO;
import com.cinepajeu.dto.VendaIngressoResponseDTO;
import com.cinepajeu.entity.AssentoReservado;
import com.cinepajeu.entity.Sessao;
import com.cinepajeu.entity.VendaIngresso;
import com.cinepajeu.exception.BusinessException;
import com.cinepajeu.mapper.ModelMapper;
import com.cinepajeu.repository.AssentoReservadoRepository;
import com.cinepajeu.repository.SessaoRepository;
import com.cinepajeu.repository.VendaIngressoRepository;
import com.cinepajeu.service.VendaIngressoService;
import com.cinepajeu.util.MapaSala;
import com.cinepajeu.util.SessaoEncerramento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaIngressoServiceImpl implements VendaIngressoService {

    private final VendaIngressoRepository vendaIngressoRepository;
    private final SessaoRepository sessaoRepository;
    private final AssentoReservadoRepository assentoReservadoRepository;

    @Override
    @Transactional
    public VendaIngressoResponseDTO registrarVenda(VendaIngressoRequestDTO request) {
        Sessao sessao = sessaoRepository.findById(request.getSessaoId())
                .orElseThrow(() -> new BusinessException("Sessão não encontrada com o ID: " + request.getSessaoId()));

        if (SessaoEncerramento.isEncerrada(sessao.getData(), sessao.getHorario())) {
            throw new BusinessException(
                    "Esta sessão já foi encerrada. As vendas encerram 10 minutos após o horário de início.");
        }

        List<String> assentos = resolverAssentos(request);

        for (String codigo : assentos) {
            if (!MapaSala.isAssentoValido(codigo)) {
                throw new BusinessException("Assento inválido: " + codigo);
            }
        }

        Set<String> codigosNormalizados = assentos.stream()
                .map(c -> c.trim().toUpperCase(Locale.ROOT))
                .collect(Collectors.toSet());

        if (codigosNormalizados.size() != assentos.size()) {
            throw new BusinessException("Não é permitido selecionar o mesmo assento mais de uma vez.");
        }

        if (assentoReservadoRepository.existsBySessaoIdAndCodigoAssentoIn(sessao.getId(), codigosNormalizados)) {
            throw new BusinessException("Um ou mais assentos selecionados já estão ocupados.");
        }

        int quantidade = assentos.size();

        if (sessao.getLugaresDisponiveis() < quantidade) {
            throw new BusinessException("Lugares insuficientes para esta sessão. Disponível: "
                    + sessao.getLugaresDisponiveis() + ", Solicitado: " + quantidade);
        }

        sessao.setLugaresDisponiveis(sessao.getLugaresDisponiveis() - quantidade);
        sessaoRepository.save(sessao);

        BigDecimal valorTotal = sessao.getValorIngresso().multiply(BigDecimal.valueOf(quantidade));

        VendaIngresso venda = VendaIngresso.builder()
                .sessao(sessao)
                .quantidade(quantidade)
                .valorTotal(valorTotal)
                .dataVenda(LocalDateTime.now())
                .build();

        VendaIngresso salva = vendaIngressoRepository.save(venda);

        List<String> assentosOrdenados = new ArrayList<>(codigosNormalizados);
        assentosOrdenados.sort(String::compareTo);

        for (String codigo : assentosOrdenados) {
            assentoReservadoRepository.save(AssentoReservado.builder()
                    .sessao(sessao)
                    .venda(salva)
                    .codigoAssento(codigo)
                    .build());
        }

        return ModelMapper.toDto(salva, assentosOrdenados);
    }

    private List<String> resolverAssentos(VendaIngressoRequestDTO request) {
        if (request.getAssentos() != null && !request.getAssentos().isEmpty()) {
            return request.getAssentos();
        }
        if (request.getQuantidade() == null || request.getQuantidade() <= 0) {
            throw new BusinessException("Informe os assentos ou a quantidade de ingressos.");
        }
        throw new BusinessException("Selecione os assentos no mapa da sala.");
    }
}
