package com.cinepajeu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaResponseDTO {
    private Long id;
    private String nome;
    private Integer capacidade;
}
