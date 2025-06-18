package com.dam.starwars.domain.service;

import com.dam.starwars.api.client.SwapiClient;
import com.dam.starwars.common.exception.FilmNotFoundException;
import com.dam.starwars.domain.model.Film;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FilmService {

    private static final Logger logger = LoggerFactory.getLogger(FilmService.class);

    private final SwapiClient swapiClient;
    private final Map<Integer, Film> filmsInMemory = new ConcurrentHashMap<>();

    public FilmService(SwapiClient swapiClient) {
        this.swapiClient = swapiClient;
    }

    @PostConstruct
    public void loadFilmsIntoMemory() {
        logger.info("Carregando filmes em memória...");

        List<Film> films = swapiClient.fetchAllFilms();

        for (Film film : films) {
            filmsInMemory.put(film.getEpisodeId(), film);
        }

        logger.info("Carregados {} filmes em memória", filmsInMemory.size());
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(filmsInMemory.values());
    }

    public Film getFilmByEpisode(Integer episodeId) {
        Film film = filmsInMemory.get(episodeId);
        if (film == null) {
            throw new FilmNotFoundException("Filme com episódio " + episodeId + " não encontrado");
        }
        return film;
    }

    public Film updateFilmDescription(Integer episodeId, String newDescription) {
        Film film = getFilmByEpisode(episodeId);

        logger.info("Atualizando descrição do filme episódio {}, versão atual: {}",
                episodeId, film.getVersion());

        film.setCustomDescription(newDescription);

        logger.info("Descrição atualizada. Nova versão: {}", film.getVersion());

        return film;
    }

    public boolean filmExists(Integer episodeId) {
        return filmsInMemory.containsKey(episodeId);
    }

    public int getTotalFilms() {
        return filmsInMemory.size();
    }
}
