package com.cinepajeu.service;

import com.cinepajeu.dto.ProdutoResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProdutoUploadService {
    ProdutoResponseDTO uploadImagem(Long produtoId, MultipartFile file);
}
