package com.dam.starwars.api.mapper;

import com.dam.starwars.api.dto.FilmDetailResponse;
import com.dam.starwars.api.dto.FilmResponse;
import com.dam.starwars.common.exception.DateParseException;
import com.dam.starwars.domain.model.Film;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class FilmMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int MAX_OPENING_CRAWL_LENGTH = 200;

    public FilmResponse toFilmResponse(Film film) {
        if (film == null) {
            return null;
        }

        return new FilmResponse(
                film.getTitle(),
                film.getEpisodeId() != null ? film.getEpisodeId() : 0,
                formatOpeningCrawl(film.getOpeningCrawl()),
                film.getDirector(),
                film.getProducer(),
                parseReleaseDate(film.getReleaseDate()),
                film.getVersion() != null ? film.getVersion() : 1,
                film.getCustomDescription(),
                film.getLastModified()
        );
    }

    public FilmDetailResponse toFilmDetailResponse(Film film) {
        return new FilmDetailResponse(
                film.getTitle(),
                film.getEpisodeId(),
                film.getOpeningCrawl(),
                film.getDirector(),
                film.getProducer(),
                film.getReleaseDate(),
                film.getCharacters(),
                film.getPlanets(),
                film.getStarships(),
                film.getVehicles(),
                film.getSpecies(),
                film.getCreated(),
                film.getEdited(),
                film.getUrl(),
                film.getVersion(),
                film.getCustomDescription(),
                film.getLastModified(),
                film.getCurrentDescription()
        );
    }

    private String formatOpeningCrawl(String openingCrawl) {
        if (openingCrawl == null || openingCrawl.trim().isEmpty()) {
            return null;
        }

        String cleaned = openingCrawl
                .replaceAll("\\s+", " ")
                .trim();

        if (cleaned.length() > MAX_OPENING_CRAWL_LENGTH) {
            return cleaned.substring(0, MAX_OPENING_CRAWL_LENGTH - 3) + "...";
        }

        return cleaned;
    }

    private LocalDate parseReleaseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new DateParseException("Formato de data inválido: " + dateString, e);
        }
    }
}