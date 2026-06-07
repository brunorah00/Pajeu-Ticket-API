package com.cinepajeu.service;

import com.cinepajeu.dto.AtualizarStatusPedidoDTO;
import com.cinepajeu.dto.VendaIngressoRequestDTO;
import com.cinepajeu.dto.VendaIngressoResponseDTO;
import com.cinepajeu.entity.StatusPedidoBomboniere;

import java.util.List;

public interface VendaIngressoService {
    VendaIngressoResponseDTO registrarVenda(VendaIngressoRequestDTO request);
    List<VendaIngressoResponseDTO> listarPedidos(StatusPedidoBomboniere status);

    List<VendaIngressoResponseDTO> listarMeusPedidos();

    VendaIngressoResponseDTO atualizarStatus(Long id, AtualizarStatusPedidoDTO request);
}
