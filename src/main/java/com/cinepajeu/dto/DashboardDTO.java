package com.cinepajeu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {

    private BigDecimal totalVendidoHoje;
    private Long totalIngressosVendidosHoje;
    private Long totalProdutosVendidosHoje;
    private Long quantidadeFilmes;
    private Long quantidadeSessoes;
    private Long quantidadeProdutos;
    private List<ProdutoResponseDTO> estoqueBomboniere;
    private List<ProdutoResponseDTO> produtosComEstoqueBaixo;
    private List<ProdutoMaisVendidoDTO> produtosMaisVendidos;
    private List<SessaoMaisVendidaDTO> sessoesMaisVendidas;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProdutoMaisVendidoDTO {
        private Long produtoId;
        private String nome;
        private Long quantidadeVendida;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessaoMaisVendidaDTO {
        private Long sessaoId;
        private String filmeTitulo;
        private Long quantidadeVendida;
    }
}
