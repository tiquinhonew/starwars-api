package com.dam.starwars.api.client;

import com.dam.starwars.domain.model.Film;
import com.dam.starwars.domain.model.SwapiFilm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class SwapiClient {

    private static final Logger logger = LoggerFactory.getLogger(SwapiClient.class);

    private final RestTemplate restTemplate;
    private final String swapiBaseUrl;
    private final String filmsEndpoint;

    public SwapiClient(RestTemplate restTemplate,
                       @Value("${swapi.base-url}") String swapiBaseUrl,
                       @Value("${swapi.films-endpoint}") String filmsEndpoint) {
        this.restTemplate = restTemplate;
        this.swapiBaseUrl = swapiBaseUrl;
        this.filmsEndpoint = filmsEndpoint;
    }

    public List<Film> fetchAllFilms() {
        try {
            String url = swapiBaseUrl + filmsEndpoint;
            logger.info("Buscando filmes da SWAPI: {}", url);

            SwapiFilm response = restTemplate.getForObject(url, SwapiFilm.class);

            if (response != null && response.getResults() != null) {
                logger.info("Encontrados {} filmes na SWAPI", response.getResults().size());
                return response.getResults();
            }

            logger.warn("Resposta vazia da SWAPI");
            return Collections.emptyList();

        } catch (RestClientException e) {
            logger.error("Erro ao buscar filmes da SWAPI: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
