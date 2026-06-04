package com.cinepajeu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmeResponseDTO {
    private Long id;
    private String titulo;
    private String genero;
    private String classificacao;
    private Integer duracao;
    private String sinopse;
    private Boolean status;
    private String urlImagem;
}
