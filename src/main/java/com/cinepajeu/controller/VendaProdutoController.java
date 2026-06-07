package com.cinepajeu.controller;

import com.cinepajeu.dto.AtualizarStatusPedidoDTO;
import com.cinepajeu.dto.VendaProdutoRequestDTO;
import com.cinepajeu.dto.VendaProdutoResponseDTO;
import com.cinepajeu.entity.StatusPedidoBomboniere;
import com.cinepajeu.service.VendaProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vendas-produto")
@RequiredArgsConstructor
@Tag(name = "Vendas de Produtos", description = "Endpoints para registro de vendas de bomboniere e produtos")
public class VendaProdutoController {

    private final VendaProdutoService vendaProdutoService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO','CLIENTE')")
    @Operation(summary = "Registrar a venda de produtos alimentícios / bomboniere")
    public ResponseEntity<VendaProdutoResponseDTO> registrarVenda(@Valid @RequestBody VendaProdutoRequestDTO request) {
        VendaProdutoResponseDTO response = vendaProdutoService.registrarVenda(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/pedidos")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @Operation(summary = "Listar pedidos da bomboniere do dia (fila do funcionário)")
    public ResponseEntity<List<VendaProdutoResponseDTO>> listarPedidos(
            @RequestParam(required = false) StatusPedidoBomboniere status) {
        return ResponseEntity.ok(vendaProdutoService.listarPedidos(status));
    }

    @GetMapping("/meus-pedidos")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO','CLIENTE')")
    @Operation(summary = "Listar pedidos da bomboniere do cliente autenticado")
    public ResponseEntity<List<VendaProdutoResponseDTO>> listarMeusPedidos() {
        return ResponseEntity.ok(vendaProdutoService.listarMeusPedidos());
    }

    @PatchMapping("/pedidos/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @Operation(summary = "Atualizar status do pedido da bomboniere")
    public ResponseEntity<VendaProdutoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusPedidoDTO request) {
        return ResponseEntity.ok(vendaProdutoService.atualizarStatus(id, request));
    }
}
