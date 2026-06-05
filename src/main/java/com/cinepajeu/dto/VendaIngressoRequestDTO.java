package com.cinepajeu.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendaIngressoRequestDTO {

    @NotNull(message = "O ID da sessão é obrigatório")
    private Long sessaoId;

    /** Quantidade (legado). Ignorada se `assentos` for informado. */
    private Integer quantidade;

    /** Códigos das poltronas (ex.: A5, F7). */
    private List<String> assentos;
}
