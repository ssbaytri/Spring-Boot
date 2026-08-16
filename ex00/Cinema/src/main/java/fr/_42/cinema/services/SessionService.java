package fr._42.cinema.services;

import fr._42.cinema.models.Session;

import java.util.List;

public interface SessionService {

    List<Session> findAll();

    Session findById(Long id);

    List<Session> findByFilmId(Long filmId);

    List<Session> findByFilmTitle(String title);

    void save(Session session);
}