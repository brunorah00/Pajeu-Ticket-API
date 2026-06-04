package com.cinepajeu.service;

import com.cinepajeu.dto.VendaProdutoRequestDTO;
import com.cinepajeu.dto.VendaProdutoResponseDTO;

public interface VendaProdutoService {
    VendaProdutoResponseDTO registrarVenda(VendaProdutoRequestDTO request);
}
