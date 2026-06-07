package com.cinepajeu.dto;

import com.cinepajeu.entity.StatusPedidoBomboniere;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendaIngressoResponseDTO {
    private Long id;
    private String codigoPedido;
    private StatusPedidoBomboniere status;
    private String clienteNome;
    private String clienteLogin;
    private SessaoResponseDTO sessao;
    private Integer quantidade;
    private BigDecimal valorTotal;
    private LocalDateTime dataVenda;
    private List<String> assentos;
}
