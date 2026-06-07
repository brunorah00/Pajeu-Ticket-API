package com.cinepajeu.controller;

import com.cinepajeu.dto.AtualizarStatusPedidoDTO;
import com.cinepajeu.dto.VendaIngressoRequestDTO;
import com.cinepajeu.dto.VendaIngressoResponseDTO;
import com.cinepajeu.entity.StatusPedidoBomboniere;
import com.cinepajeu.service.VendaIngressoService;
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
@RequestMapping("/vendas-ingresso")
@RequiredArgsConstructor
@Tag(name = "Vendas de Ingressos", description = "Endpoints para registro de vendas de ingressos")
public class VendaIngressoController {

    private final VendaIngressoService vendaIngressoService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO','CLIENTE')")
    @Operation(summary = "Registrar a venda de ingressos para uma sessão")
    public ResponseEntity<VendaIngressoResponseDTO> registrarVenda(@Valid @RequestBody VendaIngressoRequestDTO request) {
        VendaIngressoResponseDTO response = vendaIngressoService.registrarVenda(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/pedidos")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @Operation(summary = "Listar pedidos de ingressos do dia (fila do funcionário)")
    public ResponseEntity<List<VendaIngressoResponseDTO>> listarPedidos(
            @RequestParam(required = false) StatusPedidoBomboniere status) {
        return ResponseEntity.ok(vendaIngressoService.listarPedidos(status));
    }

    @GetMapping("/meus-pedidos")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO','CLIENTE')")
    @Operation(summary = "Listar solicitações de ingresso do cliente autenticado")
    public ResponseEntity<List<VendaIngressoResponseDTO>> listarMeusPedidos() {
        return ResponseEntity.ok(vendaIngressoService.listarMeusPedidos());
    }

    @PatchMapping("/pedidos/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @Operation(summary = "Atualizar status do pedido de ingresso")
    public ResponseEntity<VendaIngressoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusPedidoDTO request) {
        return ResponseEntity.ok(vendaIngressoService.atualizarStatus(id, request));
    }
}
