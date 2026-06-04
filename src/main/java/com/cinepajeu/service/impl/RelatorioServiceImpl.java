package com.cinepajeu.service.impl;

import com.cinepajeu.dto.ItemVendaProdutoResponseDTO;
import com.cinepajeu.dto.ProdutoResponseDTO;
import com.cinepajeu.dto.VendaIngressoResponseDTO;
import com.cinepajeu.dto.VendaProdutoResponseDTO;
import com.cinepajeu.mapper.ModelMapper;
import com.cinepajeu.repository.ItemVendaProdutoRepository;
import com.cinepajeu.repository.ProdutoRepository;
import com.cinepajeu.repository.VendaIngressoRepository;
import com.cinepajeu.repository.VendaProdutoRepository;
import com.cinepajeu.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioServiceImpl implements RelatorioService {

    private final VendaProdutoRepository vendaProdutoRepository;
    private final VendaIngressoRepository vendaIngressoRepository;
    private final ItemVendaProdutoRepository itemVendaProdutoRepository;
    private final ProdutoRepository produtoRepository;

    private LocalDateTime getStartDateTime(LocalDate date) {
        if (date == null) {
            return LocalDate.of(2000, 1, 1).atStartOfDay();
        }
        return date.atStartOfDay();
    }

    private LocalDateTime getEndDateTime(LocalDate date) {
        if (date == null) {
            return LocalDate.now().atTime(LocalTime.MAX);
        }
        return date.atTime(LocalTime.MAX);
    }

    @Override
    public List<VendaProdutoResponseDTO> relatorioVendas(LocalDate dataInicial, LocalDate dataFinal) {
        LocalDateTime start = getStartDateTime(dataInicial);
        LocalDateTime end = getEndDateTime(dataFinal);
        return vendaProdutoRepository.findByDataVendaBetween(start, end).stream()
                .map(ModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VendaIngressoResponseDTO> relatorioIngressos(LocalDate dataInicial, LocalDate dataFinal) {
        LocalDateTime start = getStartDateTime(dataInicial);
        LocalDateTime end = getEndDateTime(dataFinal);
        return vendaIngressoRepository.findByDataVendaBetween(start, end).stream()
                .map(ModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemVendaProdutoResponseDTO> relatorioProdutos(LocalDate dataInicial, LocalDate dataFinal) {
        LocalDateTime start = getStartDateTime(dataInicial);
        LocalDateTime end = getEndDateTime(dataFinal);
        return itemVendaProdutoRepository.findByDataVendaBetween(start, end).stream()
                .map(ModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProdutoResponseDTO> relatorioEstoque(LocalDate dataInicial, LocalDate dataFinal) {
        // Relatório de estoque lista a situação atual de todos os produtos do estoque.
        // Como o produto não armazena data de criação diretamente, retornamos todos os produtos ativos.
        return produtoRepository.findAll().stream()
                .map(ModelMapper::toDto)
                .collect(Collectors.toList());
    }
}
