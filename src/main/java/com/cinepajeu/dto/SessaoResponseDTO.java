package com.cinepajeu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessaoResponseDTO {
    private Long id;
    private FilmeResponseDTO filme;
    private LocalDate data;
    private LocalTime horario;
    private BigDecimal valorIngresso;
    private Integer lugaresDisponiveis;
}
