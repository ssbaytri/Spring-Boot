package fr._42.cinema.services;

import fr._42.cinema.models.Film;
import fr._42.cinema.repositories.FilmRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;

    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    @Override
    public Film findById(Long id) {
        return filmRepository.findById(id).orElse(null);
    }

    @Override
    public List<Film> findByTitle(String title) {
        return filmRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public void save(Film film) {
        filmRepository.save(film);
    }
}
