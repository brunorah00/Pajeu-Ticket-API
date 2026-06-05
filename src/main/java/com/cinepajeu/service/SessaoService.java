package com.cinepajeu.service;

import com.cinepajeu.dto.SessaoAssentosDTO;
import com.cinepajeu.dto.SessaoRequestDTO;
import com.cinepajeu.dto.SessaoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SessaoService {
    SessaoResponseDTO cadastrar(SessaoRequestDTO request);
    SessaoResponseDTO atualizar(Long id, SessaoRequestDTO request);
    SessaoResponseDTO buscarPorId(Long id);
    Page<SessaoResponseDTO> listar(Pageable pageable);
    SessaoAssentosDTO listarAssentos(Long sessaoId);
    void excluir(Long id);
}
