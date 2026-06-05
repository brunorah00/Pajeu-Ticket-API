package com.cinepajeu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_venda_produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendaProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_venda", nullable = false)
    private LocalDateTime dataVenda;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "codigo_pedido", unique = true, length = 24)
    private String codigoPedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pedido", length = 20, columnDefinition = "varchar(20) default 'PENDENTE'")
    @Builder.Default
    private StatusPedidoBomboniere status = StatusPedidoBomboniere.PENDENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;

    @OneToMany(mappedBy = "vendaProduto", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemVendaProduto> itens = new ArrayList<>();
}
