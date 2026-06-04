package com.cinepajeu.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class SessaoRequestDTO {

    @NotNull(message = "O ID do filme é obrigatório")
    private Long filmeId;

    /** Opcional: o cinema usa uma única sala 2D; se omitido, a sala padrão é aplicada. */
    private Long salaId;

    @NotNull(message = "A data da sessão é obrigatória")
    private LocalDate data;

    @NotNull(message = "O horário da sessão é obrigatório")
    private LocalTime horario;

    @NotNull(message = "O valor do ingresso é obrigatório")
    @DecimalMin(value = "0.0", message = "O valor do ingresso não pode ser negativo")
    private BigDecimal valorIngresso;

    @NotNull(message = "A quantidade de lugares disponíveis é obrigatória")
    @Min(value = 0, message = "A quantidade de lugares disponíveis não pode ser negativa")
    private Integer lugaresDisponiveis;
}
