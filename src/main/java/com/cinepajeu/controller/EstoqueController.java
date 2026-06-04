package com.cinepajeu.controller;

import com.cinepajeu.dto.EstoqueReporRequestDTO;
import com.cinepajeu.dto.ProdutoResponseDTO;
import com.cinepajeu.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estoque")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
@Tag(name = "Estoque", description = "Endpoints para monitoramento e reposição do estoque de produtos")
public class EstoqueController {

    private final ProdutoService produtoService;

    @GetMapping
    @Operation(summary = "Visualizar todo o estoque de produtos cadastrados")
    public ResponseEntity<Page<ProdutoResponseDTO>> listarEstoque(Pageable pageable) {
        Page<ProdutoResponseDTO> response = produtoService.listar(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/baixo")
    @Operation(summary = "Visualizar produtos com estoque abaixo do limite mínimo")
    public ResponseEntity<Page<ProdutoResponseDTO>> listarEstoqueBaixo(Pageable pageable) {
        Page<ProdutoResponseDTO> response = produtoService.listarEstoqueBaixo(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/repor")
    @Operation(summary = "Repor a quantidade de estoque de um determinado produto")
    public ResponseEntity<ProdutoResponseDTO> reporEstoque(@Valid @RequestBody EstoqueReporRequestDTO request) {
        ProdutoResponseDTO response = produtoService.reporEstoque(request.getProdutoId(), request.getQuantidade());
        return ResponseEntity.ok(response);
    }
}
