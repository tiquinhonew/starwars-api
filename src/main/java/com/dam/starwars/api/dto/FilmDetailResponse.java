package com.dam.starwars.api.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO que representa os detalhes de um filme do universo Star Wars")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FilmDetailResponse(

        @Schema(description = "Título do filme", example = "A New Hope")
        String title,

        @Schema(description = "Número do episódio", example = "4")
        @JsonProperty("episode_id")
        Integer episodeId,

        @Schema(description = "Texto da introdução (crawl)", example = "It is a period of civil war...")
        @JsonProperty("opening_crawl")
        String openingCrawl,

        @Schema(description = "Diretor do filme", example = "George Lucas")
        String director,

        @Schema(description = "Produtor do filme", example = "Gary Kurtz, Rick McCallum")
        String producer,

        @Schema(description = "Data de lançamento", example = "1977-05-25")
        @JsonProperty("release_date")
        String releaseDate,

        @Schema(description = "Lista de URLs dos personagens", example = "[\"https://swapi.py4e.com/api/people/1/\", \"https://swapi.py4e.com/api/people/2/\"]")
        List<String> characters,

        @Schema(description = "Lista de URLs dos planetas", example = "[\"https://swapi.py4e.com/api/planets/1/\", \"https://swapi.py4e.com/api/planets/2/\"]")
        List<String> planets,

        @Schema(description = "Lista de URLs das naves", example = "[\"https://swapi.py4e.com/api/starships/1/\", \"https://swapi.py4e.com/api/starships/2/\"]")
        List<String> starships,

        @Schema(description = "Lista de URLs dos veículos", example = "[\"https://swapi.py4e.com/api/vehicles/1/\", \"https://swapi.py4e.com/api/vehicles/2/\"]")
        List<String> vehicles,

        @Schema(description = "Lista de URLs das espécies", example = "[\"https://swapi.py4e.com/api/species/1/\", \"https://swapi.py4e.com/api/species/2/\"]")
        List<String> species,

        @Schema(description = "Data de criação", example = "2025-06-16T03:08:59.367")
        LocalDateTime created,

        @Schema(description = "Data de edição", example = "2025-06-16T03:08:59.367")
        LocalDateTime edited,

        @Schema(description = "URL do recurso", example = "https://swapi.py4e.com/api/films/1/")
        String url,

        @Schema(description = "Versão do recurso", example = "1")
        Integer version,

        @Schema(description = "Descrição personalizada", example = "Filme clássico da saga")
        String customDescription,

        @Schema(description = "Última modificação feita", example = "2025-06-16T03:08:59.367")
        LocalDateTime lastModified,

        @Schema(description = "Descrição atual (custom ou padrão)", example = "Introdução à saga Star Wars")
        String currentDescription

) {}
