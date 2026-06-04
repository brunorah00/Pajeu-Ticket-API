package com.cinepajeu.controller;

import com.cinepajeu.dto.VendaProdutoRequestDTO;
import com.cinepajeu.dto.VendaProdutoResponseDTO;
import com.cinepajeu.service.VendaProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendas-produto")
@RequiredArgsConstructor
@Tag(name = "Vendas de Produtos", description = "Endpoints para registro de vendas de bomboniere e produtos")
public class VendaProdutoController {

    private final VendaProdutoService vendaProdutoService;

    @PostMapping("/registrar")
    @Operation(summary = "Registrar a venda de produtos alimentícios / bomboniere")
    public ResponseEntity<VendaProdutoResponseDTO> registrarVenda(@Valid @RequestBody VendaProdutoRequestDTO request) {
        VendaProdutoResponseDTO response = vendaProdutoService.registrarVenda(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
