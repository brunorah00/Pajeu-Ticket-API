package com.cinepajeu.service;

import com.cinepajeu.dto.ItemVendaProdutoResponseDTO;
import com.cinepajeu.dto.ProdutoResponseDTO;
import com.cinepajeu.dto.VendaIngressoResponseDTO;
import com.cinepajeu.dto.VendaProdutoResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface RelatorioService {
    List<VendaProdutoResponseDTO> relatorioVendas(LocalDate dataInicial, LocalDate dataFinal);
    List<VendaIngressoResponseDTO> relatorioIngressos(LocalDate dataInicial, LocalDate dataFinal);
    List<ItemVendaProdutoResponseDTO> relatorioProdutos(LocalDate dataInicial, LocalDate dataFinal);
    List<ProdutoResponseDTO> relatorioEstoque(LocalDate dataInicial, LocalDate dataFinal);
}
