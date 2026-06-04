package com.cinepajeu.service.impl;

import com.cinepajeu.dto.VendaIngressoRequestDTO;
import com.cinepajeu.dto.VendaIngressoResponseDTO;
import com.cinepajeu.entity.Sessao;
import com.cinepajeu.entity.VendaIngresso;
import com.cinepajeu.exception.BusinessException;
import com.cinepajeu.mapper.ModelMapper;
import com.cinepajeu.repository.SessaoRepository;
import com.cinepajeu.repository.VendaIngressoRepository;
import com.cinepajeu.service.VendaIngressoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VendaIngressoServiceImpl implements VendaIngressoService {

    private final VendaIngressoRepository vendaIngressoRepository;
    private final SessaoRepository sessaoRepository;

    @Override
    @Transactional
    public VendaIngressoResponseDTO registrarVenda(VendaIngressoRequestDTO request) {
        Sessao sessao = sessaoRepository.findById(request.getSessaoId())
                .orElseThrow(() -> new BusinessException("Sessão não encontrada com o ID: " + request.getSessaoId()));

        if (sessao.getLugaresDisponiveis() < request.getQuantidade()) {
            throw new BusinessException("Lugares insuficientes para esta sessão. Disponível: " + 
                    sessao.getLugaresDisponiveis() + ", Solicitado: " + request.getQuantidade());
        }

        // Reduzir lugares disponíveis
        sessao.setLugaresDisponiveis(sessao.getLugaresDisponiveis() - request.getQuantidade());
        sessaoRepository.save(sessao);

        // Calcular valor total automaticamente
        BigDecimal valorTotal = sessao.getValorIngresso().multiply(BigDecimal.valueOf(request.getQuantidade()));

        VendaIngresso venda = VendaIngresso.builder()
                .sessao(sessao)
                .quantidade(request.getQuantidade())
                .valorTotal(valorTotal)
                .dataVenda(LocalDateTime.now())
                .build();

        VendaIngresso salva = vendaIngressoRepository.save(venda);
        return ModelMapper.toDto(salva);
    }
}
