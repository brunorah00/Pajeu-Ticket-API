package com.cinepajeu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecuperarSenhaRequestDTO {

    @NotBlank(message = "O login é obrigatório para recuperação")
    private String login;
}
