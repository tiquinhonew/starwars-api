package com.dam.starwars.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateDescriptionRequest(
        @Schema(description = "Nova descrição do filme", example = "Uma nova descrição para o filme.")
        @NotBlank(message = "Descrição não pode estar vazia")
        String description
) {
}
