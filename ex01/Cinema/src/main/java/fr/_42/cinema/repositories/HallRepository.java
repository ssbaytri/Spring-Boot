package fr._42.cinema.repositories;

import fr._42.cinema.models.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallRepository extends JpaRepository<Hall, Long> {
}
