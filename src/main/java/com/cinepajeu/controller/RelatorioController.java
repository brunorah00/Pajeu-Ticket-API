package com.cinepajeu.controller;

import com.cinepajeu.dto.ItemVendaProdutoResponseDTO;
import com.cinepajeu.dto.ProdutoResponseDTO;
import com.cinepajeu.dto.VendaIngressoResponseDTO;
import com.cinepajeu.dto.VendaProdutoResponseDTO;
import com.cinepajeu.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
@Tag(name = "Relatórios", description = "Endpoints para geração de relatórios operacionais e financeiros")
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/vendas")
    @Operation(summary = "Relatório de vendas de produtos por período")
    public ResponseEntity<List<VendaProdutoResponseDTO>> relatorioVendas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal
    ) {
        List<VendaProdutoResponseDTO> relatorio = relatorioService.relatorioVendas(dataInicial, dataFinal);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/ingressos")
    @Operation(summary = "Relatório de vendas de ingressos por período")
    public ResponseEntity<List<VendaIngressoResponseDTO>> relatorioIngressos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal
    ) {
        List<VendaIngressoResponseDTO> relatorio = relatorioService.relatorioIngressos(dataInicial, dataFinal);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/produtos")
    @Operation(summary = "Relatório detalhado de itens de bomboniere vendidos por período")
    public ResponseEntity<List<ItemVendaProdutoResponseDTO>> relatorioProdutos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal
    ) {
        List<ItemVendaProdutoResponseDTO> relatorio = relatorioService.relatorioProdutos(dataInicial, dataFinal);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/estoque")
    @Operation(summary = "Relatório de situação do estoque")
    public ResponseEntity<List<ProdutoResponseDTO>> relatorioEstoque(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal
    ) {
        List<ProdutoResponseDTO> relatorio = relatorioService.relatorioEstoque(dataInicial, dataFinal);
        return ResponseEntity.ok(relatorio);
    }
}
