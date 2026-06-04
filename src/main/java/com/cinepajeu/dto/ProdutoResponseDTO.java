package com.cinepajeu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private String categoria;
    private String descricao;
    private String urlImagem;
    private BigDecimal preco;
    private Integer quantidadeEstoque;
    private Integer estoqueMinimo;
    private Boolean ativo;
}
