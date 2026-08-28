package fr._42.cinema.dto;

import fr._42.cinema.models.Session;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class SessionMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public SessionSearchResultDTO toSearchResultDTO(Session session) {
        return new SessionSearchResultDTO(
                session.getId(),
                session.getDateTime().format(FORMATTER),
                new SessionSearchResultDTO.FilmDTO(
                        session.getFilm().getTitle(),
                        session.getFilm().getPosterUrl()));
    }
}