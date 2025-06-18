package com.dam.starwars.api.controller;

import com.dam.starwars.api.dto.FilmDetailResponse;
import com.dam.starwars.api.dto.FilmResponse;
import com.dam.starwars.api.dto.UpdateDescriptionRequest;
import com.dam.starwars.api.mapper.FilmMapper;
import com.dam.starwars.domain.model.Film;
import com.dam.starwars.domain.service.FilmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Films", description = "Operações relacionadas aos filmes Star Wars")
@RestController
@RequestMapping("/api/films")
public class FilmController {

    private final FilmService filmService;
    private final FilmMapper filmMapper;


    public FilmController(FilmService filmService, FilmMapper filmMapper) {
        this.filmService = filmService;
        this.filmMapper = filmMapper;
    }

    @Operation(summary = "Lista todos os filmes", description = "Retorna a lista completa de filmes da saga Star Wars")
    @ApiResponse(responseCode = "200", description = "Lista de filmes retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<FilmResponse>> getAllFilms() {
        List<Film> films = filmService.getAllFilms();
        List<FilmResponse> filmResponses = films.stream()
                .map(filmMapper::toFilmResponse)
                .toList();
        return ResponseEntity.ok(filmResponses);
    }

    @Operation(summary = "Busca filme por episódio", description = "Retorna detalhes de um filme específico pelo ID do episódio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filme encontrado"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado")
    })
    @GetMapping("/{episodeId}")
    public ResponseEntity<FilmDetailResponse> getFilmDetails(
            @Parameter(description = "ID do episódio do filme", example = "4")
            @PathVariable Integer episodeId
    ) {
        Film film = filmService.getFilmByEpisode(episodeId);
        FilmDetailResponse filmDetailResponse = filmMapper.toFilmDetailResponse(film);

        return ResponseEntity.ok(filmDetailResponse);
    }

    @Operation(summary = "Atualiza descrição do filme", description = "Altera a descrição de um filme e incrementa sua versão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Descrição atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado")
    })
    @PutMapping("/{episodeId}/description")
    public ResponseEntity<FilmDetailResponse> updateFilmDescription(
            @Parameter(description = "ID do episódio do filme", example = "4")
            @PathVariable Integer episodeId,
            @Parameter(description = "Nova descrição do filme")
            @Valid @RequestBody UpdateDescriptionRequest request) {

        Film updatedFilm = filmService.updateFilmDescription(episodeId, request.description());
        FilmDetailResponse updatedFilmResponse = filmMapper.toFilmDetailResponse(updatedFilm);
        return ResponseEntity.ok(updatedFilmResponse);
    }

    @Operation(summary = "Status da API", description = "Retorna informações sobre o status da API")
    @ApiResponse(responseCode = "200", description = "Status retornado com sucesso")
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        int totalFilms = filmService.getTotalFilms();

        return ResponseEntity.ok(String.format("API Star Wars funcionando. Filmes carregados: %d", totalFilms));
    }
}
