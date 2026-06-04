package com.cinepajeu.controller;

import com.cinepajeu.dto.SessaoRequestDTO;
import com.cinepajeu.dto.SessaoResponseDTO;
import com.cinepajeu.service.SessaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessoes")
@RequiredArgsConstructor
@Tag(name = "Sessões", description = "Endpoints para gerenciamento de sessões de filmes")
public class SessaoController {

    private final SessaoService sessaoService;

    @PostMapping
    @Operation(summary = "Cadastrar uma nova sessão de filme")
    public ResponseEntity<SessaoResponseDTO> cadastrar(@Valid @RequestBody SessaoRequestDTO request) {
        SessaoResponseDTO response = sessaoService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma sessão existente")
    public ResponseEntity<SessaoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody SessaoRequestDTO request) {
        SessaoResponseDTO response = sessaoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sessão por ID")
    public ResponseEntity<SessaoResponseDTO> buscarPorId(@PathVariable Long id) {
        SessaoResponseDTO response = sessaoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar sessões de forma paginada")
    public ResponseEntity<Page<SessaoResponseDTO>> listar(Pageable pageable) {
        Page<SessaoResponseDTO> response = sessaoService.listar(pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma sessão")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        sessaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
