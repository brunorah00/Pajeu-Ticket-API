package com.cinepajeu.service;

import com.cinepajeu.dto.ProdutoRequestDTO;
import com.cinepajeu.dto.ProdutoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProdutoService {
    ProdutoResponseDTO cadastrar(ProdutoRequestDTO request);
    ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO request);
    ProdutoResponseDTO buscarPorId(Long id);
    Page<ProdutoResponseDTO> listar(Pageable pageable);
    List<ProdutoResponseDTO> listarEstoqueBaixo();
    Page<ProdutoResponseDTO> listarEstoqueBaixo(Pageable pageable);
    ProdutoResponseDTO reporEstoque(Long id, Integer quantidade);
    void excluir(Long id);
}
