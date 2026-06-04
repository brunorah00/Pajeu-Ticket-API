package com.cinepajeu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmeRequestDTO {

    @NotBlank(message = "O título do filme é obrigatório")
    private String titulo;

    @NotBlank(message = "O gênero é obrigatório")
    private String genero;

    @NotBlank(message = "A classificação indicativa é obrigatória")
    private String classificacao;

    @NotNull(message = "A duração é obrigatória")
    @Positive(message = "A duração deve ser maior que zero")
    private Integer duracao;

    private String sinopse;

    @NotNull(message = "O status é obrigatório")
    private Boolean status;

    private String urlImagem;
}
