package fr._42.cinema.repositories;

import fr._42.cinema.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByFilm_TitleContainingIgnoreCase(String title);

    List<Session> findByFilmId(Long filmId);
}
