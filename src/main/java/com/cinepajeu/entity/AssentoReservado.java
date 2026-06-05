package com.cinepajeu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "tb_assento_reservado",
        uniqueConstraints = @UniqueConstraint(columnNames = {"sessao_id", "codigo_assento"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssentoReservado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_id", nullable = false)
    private Sessao sessao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venda_id", nullable = false)
    private VendaIngresso venda;

    @Column(name = "codigo_assento", nullable = false, length = 8)
    private String codigoAssento;
}
