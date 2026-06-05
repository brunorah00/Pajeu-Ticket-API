package com.cinepajeu.controller;

import com.cinepajeu.dto.FilmeRequestDTO;
import com.cinepajeu.dto.FilmeResponseDTO;
import com.cinepajeu.service.FilmeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.cinepajeu.service.FilmeUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/filmes")
@RequiredArgsConstructor
@Tag(name = "Filmes", description = "Endpoints para gerenciamento do catálogo de filmes")
public class FilmeController {

    private final FilmeService filmeService;
    private final FilmeUploadService filmeUploadService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @Operation(summary = "Cadastrar um novo filme")
    public ResponseEntity<FilmeResponseDTO> cadastrar(@Valid @RequestBody FilmeRequestDTO request) {
        FilmeResponseDTO response = filmeService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @Operation(summary = "Atualizar informações de um filme existente")
    public ResponseEntity<FilmeResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody FilmeRequestDTO request) {
        FilmeResponseDTO response = filmeService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um filme pelo seu ID")
    public ResponseEntity<FilmeResponseDTO> buscarPorId(@PathVariable Long id) {
        FilmeResponseDTO response = filmeService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar filmes de forma paginada")
    public ResponseEntity<Page<FilmeResponseDTO>> listar(Pageable pageable) {
        Page<FilmeResponseDTO> response = filmeService.listar(pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @Operation(summary = "Excluir um filme do sistema")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        filmeService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/poster", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @Operation(summary = "Upload da imagem (poster) do filme")
    public ResponseEntity<FilmeResponseDTO> uploadPoster(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(filmeUploadService.uploadPoster(id, file));
    }

    @DeleteMapping("/{id}/poster")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @Operation(summary = "Remover o poster (cartaz) do filme")
    public ResponseEntity<FilmeResponseDTO> removerPoster(@PathVariable Long id) {
        return ResponseEntity.ok(filmeUploadService.removerPoster(id));
    }
}
