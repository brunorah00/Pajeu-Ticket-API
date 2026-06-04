package com.cinepajeu.service;

import com.cinepajeu.dto.FilmeRequestDTO;
import com.cinepajeu.dto.FilmeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilmeService {
    FilmeResponseDTO cadastrar(FilmeRequestDTO request);
    FilmeResponseDTO atualizar(Long id, FilmeRequestDTO request);
    FilmeResponseDTO buscarPorId(Long id);
    Page<FilmeResponseDTO> listar(Pageable pageable);
    void excluir(Long id);
}
