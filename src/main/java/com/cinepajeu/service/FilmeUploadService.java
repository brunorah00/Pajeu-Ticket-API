package com.cinepajeu.service;

import com.cinepajeu.dto.FilmeResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FilmeUploadService {
    FilmeResponseDTO uploadPoster(Long filmeId, MultipartFile file);

    FilmeResponseDTO removerPoster(Long filmeId);
}
