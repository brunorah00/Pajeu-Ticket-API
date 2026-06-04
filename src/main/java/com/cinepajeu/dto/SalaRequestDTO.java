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
public class SalaRequestDTO {

    @NotBlank(message = "O nome da sala é obrigatório")
    private String nome;

    @NotNull(message = "A capacidade da sala é obrigatória")
    @Positive(message = "A capacidade deve ser maior que zero")
    private Integer capacidade;
}
