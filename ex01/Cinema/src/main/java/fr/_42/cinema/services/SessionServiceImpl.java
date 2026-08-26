package fr._42.cinema.services;

import fr._42.cinema.models.Session;
import fr._42.cinema.repositories.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionServiceImpl implements  SessionService {

    private final SessionRepository sessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    @Override
    public Session findById(Long id) {
        return sessionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Session> findByFilmId(Long filmId) {
        return sessionRepository.findByFilmId(filmId);
    }

    @Override
    public List<Session> findByFilmTitle(String title) {
        return sessionRepository.findByFilm_TitleContainingIgnoreCase(title);
    }

    @Override
    public void save(Session session) {
        sessionRepository.save(session);
    }
}
