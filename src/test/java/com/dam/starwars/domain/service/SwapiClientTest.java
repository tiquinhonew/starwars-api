package com.dam.starwars.domain.service;

import com.dam.starwars.api.client.SwapiClient;
import com.dam.starwars.domain.model.Film;
import com.dam.starwars.domain.model.SwapiFilm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Swapi Service Tests")
@ExtendWith(MockitoExtension.class)
class SwapiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SwapiClient swapiClient;

    private static final String BASE_URL = "https://swapi.py4e.com/api";
    private static final String FILMS_ENDPOINT = "/films/";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(swapiClient, "swapiBaseUrl", BASE_URL);
        ReflectionTestUtils.setField(swapiClient, "filmsEndpoint", FILMS_ENDPOINT);
    }

    @Test
    @DisplayName("Deve carregar todos os filmes com sucesso")
    void shouldFetchAllFilmsSuccessfully() {
        Film firstFilm = new Film("A New Hope", 4, "It is a period of civil war...");
        Film secondFilm = new Film("The Empire Strikes Back", 5, "It is a dark time...");

        SwapiFilm mockResponse = new SwapiFilm();
        mockResponse.setResults(Arrays.asList(firstFilm, secondFilm));

        String expectedUrl = BASE_URL + FILMS_ENDPOINT;
        when(restTemplate.getForObject(expectedUrl, SwapiFilm.class)).thenReturn(mockResponse);

        List<Film> result = swapiClient.fetchAllFilms();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("A New Hope", result.get(0).getTitle());
        assertEquals("The Empire Strikes Back", result.get(1).getTitle());

        verify(restTemplate, times(1)).getForObject(expectedUrl, SwapiFilm.class);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver filmes")
    void shouldReturnEmptyListWhenResponseIsNull() {
        String expectedUrl = BASE_URL + FILMS_ENDPOINT;
        when(restTemplate.getForObject(expectedUrl, SwapiFilm.class)).thenReturn(null);

        List<Film> result = swapiClient.fetchAllFilms();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando resultados forem nulos")
    void shouldReturnEmptyListWhenResultsAreNull() {
        SwapiFilm mockResponse = new SwapiFilm();
        mockResponse.setResults(null);

        String expectedUrl = BASE_URL + FILMS_ENDPOINT;
        when(restTemplate.getForObject(expectedUrl, SwapiFilm.class)).thenReturn(mockResponse);

        List<Film> result = swapiClient.fetchAllFilms();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando ocorrer RestClientException")
    void shouldReturnEmptyListWhenRestClientExceptionOccurs() {
        String expectedUrl = BASE_URL + FILMS_ENDPOINT;
        when(restTemplate.getForObject(expectedUrl, SwapiFilm.class))
                .thenThrow(new RestClientException("Connection failed"));

        List<Film> result = swapiClient.fetchAllFilms();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}