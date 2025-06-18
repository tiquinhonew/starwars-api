package com.dam.starwars.domain.service;

import com.dam.starwars.api.client.SwapiClient;
import com.dam.starwars.common.exception.FilmNotFoundException;
import com.dam.starwars.domain.model.Film;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Film Service Tests")
@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @Mock
    private SwapiClient swapiClient;

    @InjectMocks
    private FilmService filmService;

    private List<Film> mockFilms;

    @BeforeEach
    void setUp() {
        Film firstFilm = new Film("A New Hope", 4, "It is a period of civil war...");
        Film secondFilm = new Film("The Empire Strikes Back", 5, "It is a dark time for the Rebellion...");

        mockFilms = Arrays.asList(firstFilm, secondFilm);
    }

    @Test
    @DisplayName("Deve carregar filmes na memória após a inicialização")
    void shouldLoadFilmsIntoMemoryOnPostConstruct() {
        when(swapiClient.fetchAllFilms()).thenReturn(mockFilms);

        filmService.loadFilmsIntoMemory();

        List<Film> films = filmService.getAllFilms();
        assertEquals(2, films.size());
        assertTrue(filmService.filmExists(4));
        assertTrue(filmService.filmExists(5));
    }

    @Test
    @DisplayName("Deve retornar todos os filmes")
    void shouldReturnAllFilms() {
        when(swapiClient.fetchAllFilms()).thenReturn(mockFilms);
        filmService.loadFilmsIntoMemory();

        List<Film> result = filmService.getAllFilms();

        assertEquals(2, result.size());
        assertEquals("A New Hope", result.get(0).getTitle());
    }

    @Test
    @DisplayName("Deve retornar filme por ID de episódio")
    void shouldReturnFilmByEpisodeId() {
        when(swapiClient.fetchAllFilms()).thenReturn(mockFilms);
        filmService.loadFilmsIntoMemory();

        Film result = filmService.getFilmByEpisode(4);

        assertNotNull(result);
        assertEquals("A New Hope", result.getTitle());
        assertEquals(4, result.getEpisodeId());
    }

    @Test
    @DisplayName("Deve lançar exceção quando filme não encontrado por ID de episódio")
    void shouldThrowExceptionWhenFilmNotFound() {
        when(swapiClient.fetchAllFilms()).thenReturn(mockFilms);
        filmService.loadFilmsIntoMemory();

        assertThrows(FilmNotFoundException.class, () -> {
            filmService.getFilmByEpisode(999);
        });
    }

    @Test
    @DisplayName("Deve atualizar descrição do filme e incrementar versão")
    void shouldUpdateFilmDescriptionAndIncrementVersion() {
        when(swapiClient.fetchAllFilms()).thenReturn(mockFilms);
        filmService.loadFilmsIntoMemory();
        String newDescription = "Nova descrição personalizada";

        Film result = filmService.updateFilmDescription(4, newDescription);

        assertNotNull(result);
        assertEquals(newDescription, result.getCustomDescription());
        assertEquals(newDescription, result.getCurrentDescription());
        assertEquals(2, result.getVersion());
    }

    @Test
    @DisplayName("Deve incrementar versão múltiplas vezes")
    void shouldIncrementVersionMultipleTimes() {
        when(swapiClient.fetchAllFilms()).thenReturn(mockFilms);
        filmService.loadFilmsIntoMemory();

        filmService.updateFilmDescription(4, "Primeira atualização");
        filmService.updateFilmDescription(4, "Segunda atualização");
        Film result = filmService.updateFilmDescription(4, "Terceira atualização");

        assertEquals(4, result.getVersion());
        assertEquals("Terceira atualização", result.getCurrentDescription());
    }

    @Test
    @DisplayName("Deve retornar o total de filmes")
    void shouldReturnCorrectTotalFilms() {
        when(swapiClient.fetchAllFilms()).thenReturn(mockFilms);
        filmService.loadFilmsIntoMemory();

        int total = filmService.getTotalFilms();

        assertEquals(2, total);
    }
}