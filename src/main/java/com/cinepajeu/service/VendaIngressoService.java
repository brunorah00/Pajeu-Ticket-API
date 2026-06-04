package com.cinepajeu.service;

import com.cinepajeu.dto.VendaIngressoRequestDTO;
import com.cinepajeu.dto.VendaIngressoResponseDTO;

public interface VendaIngressoService {
    VendaIngressoResponseDTO registrarVenda(VendaIngressoRequestDTO request);
}
