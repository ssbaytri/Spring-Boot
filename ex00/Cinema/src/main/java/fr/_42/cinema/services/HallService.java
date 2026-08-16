package fr._42.cinema.services;

import fr._42.cinema.models.Hall;

import java.util.List;

public interface HallService {

    List<Hall> findAll();

    Hall findById(Long id);

    void save(Hall hall);
}