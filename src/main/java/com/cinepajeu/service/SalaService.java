package com.cinepajeu.service;

import com.cinepajeu.dto.SalaRequestDTO;
import com.cinepajeu.dto.SalaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SalaService {
    SalaResponseDTO cadastrar(SalaRequestDTO request);
    SalaResponseDTO atualizar(Long id, SalaRequestDTO request);
    SalaResponseDTO buscarPorId(Long id);
    Page<SalaResponseDTO> listar(Pageable pageable);
    void excluir(Long id);
}
