package com.cinepajeu.dto;

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
public class VendaIngressoRequestDTO {

    @NotNull(message = "O ID da sessão é obrigatório")
    private Long sessaoId;

    @NotNull(message = "A quantidade de ingressos é obrigatória")
    @Positive(message = "A quantidade de ingressos deve ser maior que zero")
    private Integer quantidade;
}
