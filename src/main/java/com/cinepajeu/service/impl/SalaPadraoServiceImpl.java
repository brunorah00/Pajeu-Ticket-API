package com.cinepajeu.service.impl;

import com.cinepajeu.entity.Sala;
import com.cinepajeu.repository.SalaRepository;
import com.cinepajeu.service.SalaPadraoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalaPadraoServiceImpl implements SalaPadraoService {

    private static final String NOME_SALA = "Tela 2D";
    private static final int CAPACIDADE = 200;

    private final SalaRepository salaRepository;

    @Override
    @Transactional
    public Sala obterSalaUnica() {
        return salaRepository.findAll().stream()
                .filter(s -> NOME_SALA.equalsIgnoreCase(s.getNome()))
                .findFirst()
                .or(() -> salaRepository.findAll().stream().findFirst())
                .orElseGet(() -> salaRepository.save(
                        Sala.builder().nome(NOME_SALA).capacidade(CAPACIDADE).build()));
    }
}
