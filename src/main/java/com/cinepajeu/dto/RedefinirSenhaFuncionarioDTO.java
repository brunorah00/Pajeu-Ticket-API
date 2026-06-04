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
public class RedefinirSenhaFuncionarioDTO {

    @NotBlank(message = "A nova senha é obrigatória")
    private String senha;
}
