package com.dam.starwars.domain.model;

import java.util.List;

public class SwapiFilm {
    private List<Film> results;

    public List<Film> getResults() {
        return results;
    }

    public void setResults(List<Film> results) {
        this.results = results;
    }
}
