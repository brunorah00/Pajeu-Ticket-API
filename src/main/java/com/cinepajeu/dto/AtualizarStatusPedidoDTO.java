package com.cinepajeu.dto;

import com.cinepajeu.entity.StatusPedidoBomboniere;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarStatusPedidoDTO {

    @NotNull(message = "O status é obrigatório")
    private StatusPedidoBomboniere status;
}
