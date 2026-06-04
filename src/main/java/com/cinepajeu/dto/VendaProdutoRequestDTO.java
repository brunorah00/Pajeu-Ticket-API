package com.cinepajeu.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendaProdutoRequestDTO {

    @NotEmpty(message = "A venda deve conter pelo menos um item")
    @Valid
    private List<ItemVendaProdutoRequestDTO> itens;
}
