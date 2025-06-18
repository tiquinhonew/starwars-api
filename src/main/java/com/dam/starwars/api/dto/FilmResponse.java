package com.dam.starwars.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "DTO que representa os dados de um filme do Star Wars")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FilmResponse(
        @Schema(description = "Título do filme", example = "A New Hope")
        String title,

        @Schema(description = "Número do episódio", example = "4")
        int episode_id,

        @Schema(description = "Texto da introdução (crawl)", example = "It is a period of civil war...")
        String opening_crawl,

        @Schema(description = "Diretor do filme", example = "George Lucas")
        String director,

        @Schema(description = "Produtor do filme", example = "Gary Kurtz, Rick McCallum")
        String producer,

        @Schema(description = "Data de lançamento do filme", example = "1977-05-25")
        LocalDate release_date,

        @Schema(description = "Versão do recurso", example = "1")
        int version,

        @Schema(description = "Descrição personalizada", example = "Filme clássico da saga")
        String customDescription,

        @Schema(description = "Última modificação feita", example = "2025-06-16T03:08:59.367")
        LocalDateTime lastModified
) {
}
