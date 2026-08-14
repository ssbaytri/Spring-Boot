package fr._42.cinema.repositories;

import fr._42.cinema.models.AuthenticationLog;
import fr._42.cinema.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthenticationLogRepository extends JpaRepository<AuthenticationLog, Long> {

    List<AuthenticationLog> findAllByUserOrderByAuthenticatedAtDesc(User user);
}

