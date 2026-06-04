package com.cinepajeu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_filme")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String genero;

    @Column(nullable = false)
    private String classificacao;

    @Column(nullable = false)
    private Integer duracao;

    @Column(columnDefinition = "TEXT")
    private String sinopse;

    @Column(nullable = false)
    private Boolean status;

    @Column(name = "url_imagem")
    private String urlImagem;
}
