package com.cinepajeu.controller;

import com.cinepajeu.dto.DashboardDTO;
import com.cinepajeu.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Endpoints para consolidação de estatísticas e métricas diárias")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO')")
    @Operation(summary = "Obter os dados consolidados do dashboard do cinema")
    public ResponseEntity<DashboardDTO> obterDashboard() {
        DashboardDTO dashboard = dashboardService.obterDashboard();
        return ResponseEntity.ok(dashboard);
    }
}
