package com.cinepajeu.service;

import com.cinepajeu.dto.AtualizarStatusPedidoDTO;
import com.cinepajeu.dto.VendaProdutoRequestDTO;
import com.cinepajeu.dto.VendaProdutoResponseDTO;
import com.cinepajeu.entity.StatusPedidoBomboniere;

import java.util.List;

public interface VendaProdutoService {
    VendaProdutoResponseDTO registrarVenda(VendaProdutoRequestDTO request);

    List<VendaProdutoResponseDTO> listarPedidos(StatusPedidoBomboniere status);

    VendaProdutoResponseDTO atualizarStatus(Long id, AtualizarStatusPedidoDTO request);
}
