package com.cinepajeu.service.impl;

import com.cinepajeu.dto.SessaoAssentosDTO;
import com.cinepajeu.dto.SessaoRequestDTO;
import com.cinepajeu.dto.SessaoResponseDTO;
import com.cinepajeu.entity.Filme;
import com.cinepajeu.entity.Sala;
import com.cinepajeu.entity.Sessao;
import com.cinepajeu.exception.BusinessException;
import com.cinepajeu.mapper.ModelMapper;
import com.cinepajeu.repository.AssentoReservadoRepository;
import com.cinepajeu.repository.FilmeRepository;
import com.cinepajeu.repository.SalaRepository;
import com.cinepajeu.repository.SessaoRepository;
import com.cinepajeu.repository.VendaIngressoRepository;
import com.cinepajeu.service.SalaPadraoService;
import com.cinepajeu.service.SessaoService;
import com.cinepajeu.util.MapaSala;
import com.cinepajeu.util.SessaoHorarioConflito;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessaoServiceImpl implements SessaoService {

    private final SessaoRepository sessaoRepository;
    private final AssentoReservadoRepository assentoReservadoRepository;
    private final FilmeRepository filmeRepository;
    private final SalaRepository salaRepository;
    private final VendaIngressoRepository vendaIngressoRepository;
    private final SalaPadraoService salaPadraoService;

    private Sala resolverSala(Long salaId) {
        if (salaId != null) {
            return salaRepository.findById(salaId)
                    .orElseThrow(() -> new BusinessException("Sala não encontrada com o ID: " + salaId));
        }
        return salaPadraoService.obterSalaUnica();
    }

    @Override
    @Transactional
    public SessaoResponseDTO cadastrar(SessaoRequestDTO request) {
        Filme filme = filmeRepository.findById(request.getFilmeId())
                .orElseThrow(() -> new BusinessException("Filme não encontrado com o ID: " + request.getFilmeId()));

        Sala sala = resolverSala(request.getSalaId());

        if (request.getLugaresDisponiveis() > sala.getCapacidade()) {
            throw new BusinessException("A quantidade de lugares disponíveis (" + request.getLugaresDisponiveis() + 
                    ") não pode ser maior que a capacidade da sala (" + sala.getCapacidade() + ")");
        }

        SessaoHorarioConflito.validar(
                filme,
                request.getData(),
                request.getHorario(),
                sessaoRepository.findByData(request.getData()),
                null
        );

        Sessao sessao = ModelMapper.toEntity(request, filme, sala);
        Sessao salva = sessaoRepository.save(sessao);
        return ModelMapper.toDto(salva);
    }

    @Override
    @Transactional
    public SessaoResponseDTO atualizar(Long id, SessaoRequestDTO request) {
        Sessao sessao = sessaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Sessão não encontrada com o ID: " + id));

        Filme filme = filmeRepository.findById(request.getFilmeId())
                .orElseThrow(() -> new BusinessException("Filme não encontrado com o ID: " + request.getFilmeId()));

        Sala sala = resolverSala(request.getSalaId());

        if (request.getLugaresDisponiveis() > sala.getCapacidade()) {
            throw new BusinessException("A quantidade de lugares disponíveis (" + request.getLugaresDisponiveis() + 
                    ") não pode ser maior que a capacidade da sala (" + sala.getCapacidade() + ")");
        }

        SessaoHorarioConflito.validar(
                filme,
                request.getData(),
                request.getHorario(),
                sessaoRepository.findByData(request.getData()),
                id
        );

        sessao.setFilme(filme);
        sessao.setSala(sala);
        sessao.setData(request.getData());
        sessao.setHorario(request.getHorario());
        sessao.setValorIngresso(request.getValorIngresso());
        sessao.setLugaresDisponiveis(request.getLugaresDisponiveis());

        Sessao atualizada = sessaoRepository.save(sessao);
        return ModelMapper.toDto(atualizada);
    }

    @Override
    public SessaoResponseDTO buscarPorId(Long id) {
        Sessao sessao = sessaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Sessão não encontrada com o ID: " + id));
        return ModelMapper.toDto(sessao);
    }

    @Override
    public Page<SessaoResponseDTO> listar(Pageable pageable) {
        return sessaoRepository.findAll(pageable).map(ModelMapper::toDto);
    }

    @Override
    public SessaoAssentosDTO listarAssentos(Long sessaoId) {
        if (!sessaoRepository.existsById(sessaoId)) {
            throw new BusinessException("Sessão não encontrada com o ID: " + sessaoId);
        }
        List<String> ocupados = assentoReservadoRepository.findBySessaoId(sessaoId).stream()
                .map(a -> a.getCodigoAssento())
                .sorted()
                .toList();
        return SessaoAssentosDTO.builder()
                .ocupados(ocupados)
                .totalAssentos(MapaSala.getAssentosValidos().size())
                .build();
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (!sessaoRepository.existsById(id)) {
            throw new BusinessException("Sessão não encontrada com o ID: " + id);
        }
        vendaIngressoRepository.deleteBySessaoId(id);
        sessaoRepository.deleteById(id);
    }
}
