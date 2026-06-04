package com.cinepajeu.controller;

import com.cinepajeu.dto.VendaIngressoRequestDTO;
import com.cinepajeu.dto.VendaIngressoResponseDTO;
import com.cinepajeu.service.VendaIngressoService;
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
@RequestMapping("/vendas-ingresso")
@RequiredArgsConstructor
@Tag(name = "Vendas de Ingressos", description = "Endpoints para registro de vendas de ingressos")
public class VendaIngressoController {

    private final VendaIngressoService vendaIngressoService;

    @PostMapping("/registrar")
    @Operation(summary = "Registrar a venda de ingressos para uma sessão")
    public ResponseEntity<VendaIngressoResponseDTO> registrarVenda(@Valid @RequestBody VendaIngressoRequestDTO request) {
        VendaIngressoResponseDTO response = vendaIngressoService.registrarVenda(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
