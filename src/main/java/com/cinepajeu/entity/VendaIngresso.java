package com.cinepajeu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_venda_ingresso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendaIngresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_id", nullable = false)
    private Sessao sessao;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "data_venda", nullable = false)
    private LocalDateTime dataVenda;

    @Column(name = "codigo_pedido", unique = true, length = 24)
    private String codigoPedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pedido", length = 20, columnDefinition = "varchar(20) default 'PENDENTE'")
    @Builder.Default
    private StatusPedidoBomboniere status = StatusPedidoBomboniere.PENDENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;
}
