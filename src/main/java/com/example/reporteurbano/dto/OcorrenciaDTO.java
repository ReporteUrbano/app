package com.example.reporteurbano.dto;

import jakarta.validation.constraints.*;

public record OcorrenciaDTO(
        @NotBlank String tituloProblema,
        @NotBlank String descricao,
        @NotBlank @Pattern(regexp = "^-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?$", message = "Localização deve estar no formato 'latitude,longitude'") String localizacao,
        @NotBlank String foto,
        @Min(1) int userId,
        @NotBlank String categoria
) {
}