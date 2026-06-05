package com.cinepajeu.dto;

import com.cinepajeu.entity.StatusPedidoBomboniere;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendaProdutoResponseDTO {
    private Long id;
    private String codigoPedido;
    private StatusPedidoBomboniere status;
    private String clienteNome;
    private String clienteLogin;
    private LocalDateTime dataVenda;
    private BigDecimal valorTotal;
    private List<ItemVendaProdutoResponseDTO> itens;
}
