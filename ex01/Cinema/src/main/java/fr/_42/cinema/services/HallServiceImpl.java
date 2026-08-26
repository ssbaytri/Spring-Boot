package fr._42.cinema.services;

import fr._42.cinema.models.Hall;
import fr._42.cinema.repositories.HallRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;

    public HallServiceImpl(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    @Override
    public List<Hall> findAll() {
        return hallRepository.findAll();
    }

    @Override
    public Hall findById(Long id) {
        return hallRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Hall hall) {
        hallRepository.save(hall);
    }
}
