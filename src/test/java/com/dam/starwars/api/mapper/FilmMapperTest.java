package com.dam.starwars.api.mapper;

import com.dam.starwars.api.dto.FilmResponse;
import com.dam.starwars.common.exception.DateParseException;
import com.dam.starwars.domain.model.Film;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Film Mapper Tests")
class FilmMapperTest {

    private FilmMapper filmMapper;

    @BeforeEach
    void setUp() {
        filmMapper = new FilmMapper();
    }

    @Test
    @DisplayName("Deve mapear Film para FilmResponse corretamente")
    void shouldMapFilmToFilmResponseCorrectly() {
        Film film = createSampleFilm();

        FilmResponse result = filmMapper.toFilmResponse(film);

        assertNotNull(result);
        assertEquals("A New Hope", result.title());
        assertEquals(4, result.episode_id());
        assertEquals("It is a period of civil war...", result.opening_crawl());
        assertEquals("George Lucas", result.director());
        assertEquals("Gary Kurtz, Rick McCallum", result.producer());
        assertEquals(LocalDate.of(1977, 5, 25), result.release_date());
        assertEquals(2, result.version());
        assertEquals("Filme clássico", result.customDescription());
        assertEquals(film.getLastModified(), result.lastModified());
    }

    @Test
    @DisplayName("Deve retornar null quando Film for null")
    void shouldReturnNullWhenFilmIsNull() {
        FilmResponse result = filmMapper.toFilmResponse(null);

        assertNull(result);
    }

    @Test
    @DisplayName("Deve lidar com campos nulos no Film")
    void shouldHandleNullFieldsInFilm() {
        Film film = new Film();
        film.setTitle("Test Title");

        FilmResponse result = filmMapper.toFilmResponse(film);

        assertNotNull(result);
        assertEquals("Test Title", result.title());
        assertEquals(0, result.episode_id());
        assertEquals(1, result.version());
        assertNull(result.opening_crawl());
        assertNull(result.director());
        assertNull(result.producer());
        assertNull(result.release_date());
        assertNull(result.customDescription());
    }

    @Test
    @DisplayName("Deve lançar exceção para data de lançamento inválida")
    void shouldThrowExceptionForInvalidReleaseDate() {
        Film film = new Film();
        film.setTitle("Test Title");
        film.setReleaseDate("invalid-date");

        assertThrows(DateParseException.class, () -> {
            filmMapper.toFilmResponse(film);
        });
    }

    @Test
    @DisplayName("Deve lidar com data de lançamento vazia")
    void shouldHandleEmptyReleaseDate() {
        Film film = new Film();
        film.setTitle("Test Title");
        film.setReleaseDate("");

        FilmResponse result = filmMapper.toFilmResponse(film);

        assertNotNull(result);
        assertNull(result.release_date());
    }

    private Film createSampleFilm() {
        Film film = new Film();
        film.setTitle("A New Hope");
        film.setEpisodeId(4);
        film.setOpeningCrawl("It is a period of civil war...");
        film.setDirector("George Lucas");
        film.setProducer("Gary Kurtz, Rick McCallum");
        film.setReleaseDate("1977-05-25");
        film.setCharacters(List.of("https://swapi.dev/api/people/1/"));
        film.setPlanets(List.of("https://swapi.dev/api/planets/1/"));
        film.setStarships(List.of("https://swapi.dev/api/starships/2/"));
        film.setVehicles(List.of("https://swapi.dev/api/vehicles/4/"));
        film.setSpecies(List.of("https://swapi.dev/api/species/1/"));
        film.setVersion(1);
        film.setCustomDescription("Filme clássico");
        film.setLastModified(LocalDateTime.now());
        return film;
    }
}