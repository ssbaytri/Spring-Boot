package fr._42.cinema.services;

import fr._42.cinema.models.Film;

import java.util.List;

public interface FilmService {
    List<Film> findAll();

    Film findById(Long id);

    List<Film> findByTitle(String title);

    void save(Film film);
}