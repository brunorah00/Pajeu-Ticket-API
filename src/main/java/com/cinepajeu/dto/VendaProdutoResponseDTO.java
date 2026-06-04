package com.cinepajeu.dto;

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
    private LocalDateTime dataVenda;
    private BigDecimal valorTotal;
    private List<ItemVendaProdutoResponseDTO> itens;
}
