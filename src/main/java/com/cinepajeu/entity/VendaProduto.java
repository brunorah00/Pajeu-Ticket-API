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

    @OneToMany(mappedBy = "vendaProduto", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemVendaProduto> itens = new ArrayList<>();
}
