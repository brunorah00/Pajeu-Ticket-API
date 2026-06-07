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
public class OAuthLoginRequestDTO {

    @NotBlank(message = "O provedor é obrigatório")
    private String provider;

    @NotBlank(message = "O token de acesso é obrigatório")
    private String token;
}
