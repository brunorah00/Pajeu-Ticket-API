package com.cinepajeu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidarTokenRecuperacaoDTO {
    private boolean valido;
    private String login;
}
