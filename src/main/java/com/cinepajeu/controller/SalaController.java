package com.cinepajeu.controller;

import com.cinepajeu.dto.SalaRequestDTO;
import com.cinepajeu.dto.SalaResponseDTO;
import com.cinepajeu.service.SalaService;
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
@RequestMapping("/salas")
@RequiredArgsConstructor
@Tag(name = "Salas", description = "Endpoints para gerenciamento das salas de exibição")
public class SalaController {

    private final SalaService salaService;

    @PostMapping
    @Operation(summary = "Cadastrar uma nova sala")
    public ResponseEntity<SalaResponseDTO> cadastrar(@Valid @RequestBody SalaRequestDTO request) {
        SalaResponseDTO response = salaService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma sala existente")
    public ResponseEntity<SalaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody SalaRequestDTO request) {
        SalaResponseDTO response = salaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sala por ID")
    public ResponseEntity<SalaResponseDTO> buscarPorId(@PathVariable Long id) {
        SalaResponseDTO response = salaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar salas de forma paginada")
    public ResponseEntity<Page<SalaResponseDTO>> listar(Pageable pageable) {
        Page<SalaResponseDTO> response = salaService.listar(pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma sala")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        salaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
