package fr._42.cinema.repositories;

import fr._42.cinema.models.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {

    List<Film> findByTitleContainingIgnoreCase(String title);
}
