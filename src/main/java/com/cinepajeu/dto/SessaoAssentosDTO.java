package com.cinepajeu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessaoAssentosDTO {
    private List<String> ocupados;
    private int totalAssentos;
}
