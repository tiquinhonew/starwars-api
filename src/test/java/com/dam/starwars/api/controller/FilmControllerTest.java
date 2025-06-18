package com.dam.starwars.api.controller;

import com.dam.starwars.api.dto.FilmDetailResponse;
import com.dam.starwars.api.dto.FilmResponse;
import com.dam.starwars.api.dto.UpdateDescriptionRequest;
import com.dam.starwars.api.mapper.FilmMapper;
import com.dam.starwars.common.exception.FilmNotFoundException;
import com.dam.starwars.domain.model.Film;
import com.dam.starwars.domain.service.FilmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Film Controller Tests")
@WebMvcTest(FilmController.class)
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    @MockBean
    private FilmMapper filmMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Film> mockFilms;
    private Film mockFilm;
    private List<FilmResponse> mockFilmResponses;

    @BeforeEach
    void setUp() {
        mockFilm = new Film("A New Hope", 4, "It is a period of civil war...");
        mockFilm.setDirector("George Lucas");
        mockFilm.setProducer("Gary Kurtz, Rick McCallum");
        mockFilm.setReleaseDate("1977-05-25");
        mockFilm.setVersion(1);
        mockFilm.setLastModified(LocalDateTime.now());

        Film secondFilm = new Film("The Empire Strikes Back", 5, "It is a dark time...");
        secondFilm.setDirector("Irvin Kershner");
        secondFilm.setProducer("Gary Kurtz, Rick McCallum");
        secondFilm.setReleaseDate("1980-05-17");
        secondFilm.setVersion(1);
        secondFilm.setLastModified(LocalDateTime.now());

        mockFilms = Arrays.asList(mockFilm, secondFilm);

        FilmResponse mockFilmResponse = new FilmResponse(
                "A New Hope",
                4,
                "It is a period of civil war...",
                "George Lucas",
                "Gary Kurtz, Rick McCallum",
                LocalDate.of(1977, 5, 25),
                1,
                null,
                LocalDateTime.now()
        );

        FilmResponse secondFilmResponse = new FilmResponse(
                "The Empire Strikes Back",
                5,
                "It is a dark time...",
                "Irvin Kershner",
                "Gary Kurtz, Rick McCallum",
                LocalDate.of(1980, 5, 17),
                1,
                null,
                LocalDateTime.now()
        );

        mockFilmResponses = Arrays.asList(mockFilmResponse, secondFilmResponse);
    }

    @Test
    @DisplayName("Deve retornar a lista de filmes")
    void shouldGetAllFilms() throws Exception {
        when(filmService.getAllFilms()).thenReturn(mockFilms);
        when(filmMapper.toFilmResponse(mockFilms.get(0))).thenReturn(mockFilmResponses.get(0));
        when(filmMapper.toFilmResponse(mockFilms.get(1))).thenReturn(mockFilmResponses.get(1));

        mockMvc.perform(get("/api/films"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("A New Hope"))
                .andExpect(jsonPath("$[0].episode_id").value(4))
                .andExpect(jsonPath("$[1].title").value("The Empire Strikes Back"))
                .andExpect(jsonPath("$[1].episode_id").value(5));
    }

    @Test
    @DisplayName("Deve retornar detalhes do filme por episódio")
    void shouldGetFilmDetails() throws Exception {
        FilmDetailResponse updatedFilmResponse = new FilmDetailResponse(
                "A New Hope",
                4,
                "It is a period of civil war...",
                "George Lucas",
                "Gary Kurtz, Rick McCallum",
                LocalDate.of(1977, 5, 25).toString(),
                List.of("https://swapi.py4e.com/api/people/1/", "https://swapi.py4e.com/api/people/2/"),
                List.of("https://swapi.py4e.com/api/planets/1/", "https://swapi.py4e.com/api/planets/2/"),
                List.of("https://swapi.py4e.com/api/starships/1/", "https://swapi.py4e.com/api/starships/2/"),
                List.of("https://swapi.py4e.com/api/vehicles/1/", "https://swapi.py4e.com/api/vehicles/2/"),
                List.of("https://swapi.py4e.com/api/species/1/", "https://swapi.py4e.com/api/species/2/"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "https://swapi.py4e.com/api/films/4/",
                2,
                "Nova descrição personalizada",
                LocalDateTime.now(),
                "Introdução à saga Star Wars"
        );

        when(filmService.getFilmByEpisode(4)).thenReturn(mockFilm);
        when(filmMapper.toFilmDetailResponse(mockFilm)).thenReturn(updatedFilmResponse);

        mockMvc.perform(get("/api/films/4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("A New Hope"))
                .andExpect(jsonPath("$.episode_id").value(4))
                .andExpect(jsonPath("$.version").value(2));
    }

    @Test
    @DisplayName("Deve retornar erro 404 quando filme não existe")
    void shouldReturnNotFoundWhenFilmDoesNotExist() throws Exception {
        when(filmService.getFilmByEpisode(999))
                .thenThrow(new FilmNotFoundException("Filme com episódio 999 não encontrado"));

        mockMvc.perform(get("/api/films/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Filme Não Encontrado"))
                .andExpect(jsonPath("$.message").value("Filme com episódio 999 não encontrado"));
    }

    @Test
    @DisplayName("Deve atualizar a descrição do filme")
    void shouldUpdateFilmDescription() throws Exception {
        String newDescription = "Nova descrição personalizada";
        UpdateDescriptionRequest request = new UpdateDescriptionRequest(newDescription);

        Film updatedFilm = new Film("A New Hope", 4, "It is a period of civil war...");
        updatedFilm.setCustomDescription(newDescription);
        updatedFilm.setVersion(2);
        updatedFilm.setLastModified(LocalDateTime.now());

        FilmDetailResponse updatedFilmResponse = new FilmDetailResponse(
                "A New Hope",
                4,
                "It is a period of civil war...",
                "George Lucas",
                "Gary Kurtz, Rick McCallum",
                LocalDate.of(1977, 5, 25).toString(),
                List.of("https://swapi.py4e.com/api/people/1/", "https://swapi.py4e.com/api/people/2/"),
                List.of("https://swapi.py4e.com/api/planets/1/", "https://swapi.py4e.com/api/planets/2/"),
                List.of("https://swapi.py4e.com/api/starships/1/", "https://swapi.py4e.com/api/starships/2/"),
                List.of("https://swapi.py4e.com/api/vehicles/1/", "https://swapi.py4e.com/api/vehicles/2/"),
                List.of("https://swapi.py4e.com/api/species/1/", "https://swapi.py4e.com/api/species/2/"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "https://swapi.py4e.com/api/films/4/",
                2,
                newDescription,
                LocalDateTime.now(),
                "Introdução à saga Star Wars"
        );

        when(filmService.updateFilmDescription(eq(4), eq(newDescription))).thenReturn(updatedFilm);
        when(filmMapper.toFilmDetailResponse(updatedFilm)).thenReturn(updatedFilmResponse);

        mockMvc.perform(put("/api/films/4/description")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("A New Hope"))
                .andExpect(jsonPath("$.episode_id").value(4))
                .andExpect(jsonPath("$.customDescription").value(newDescription))
                .andExpect(jsonPath("$.version").value(2));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando descrição é inválida")
    void shouldReturnBadRequestWhenDescriptionIsEmpty() throws Exception {
        UpdateDescriptionRequest request = new UpdateDescriptionRequest("");

        mockMvc.perform(put("/api/films/4/description")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Falha de Validação"))
                .andExpect(jsonPath("$.fieldErrors.description").value("Descrição não pode estar vazia"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando descrição é nula")
    void shouldReturnBadRequestWhenDescriptionIsNull() throws Exception {
        UpdateDescriptionRequest request = new UpdateDescriptionRequest(null);

        mockMvc.perform(put("/api/films/4/description")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Falha de Validação"));
    }

    @Test
    @DisplayName("Deve retornar erro 404 ao tentar atualizar filme inexistente")
    void shouldReturnNotFoundWhenUpdatingNonExistentFilm() throws Exception {
        String newDescription = "Nova descrição";
        UpdateDescriptionRequest request = new UpdateDescriptionRequest(newDescription);

        when(filmService.updateFilmDescription(eq(999), eq(newDescription)))
                .thenThrow(new FilmNotFoundException("Filme com episódio 999 não encontrado"));

        mockMvc.perform(put("/api/films/999/description")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Filme Não Encontrado"));
    }

    @Test
    @DisplayName("Deve retornar status da API")
    void shouldGetApiStatus() throws Exception {
        when(filmService.getTotalFilms()).thenReturn(6);

        mockMvc.perform(get("/api/films/status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("API Star Wars funcionando. Filmes carregados: 6"));
    }
}