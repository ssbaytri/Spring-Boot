package fr._42.cinema.repositories;

import fr._42.cinema.models.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Usage: chatMessageRepository.findByFilmIdOrderBySentAtDesc(filmId, PageRequest.of(0, limit))
    List<ChatMessage> findByFilmIdOrderBySentAtDesc(Long filmId, Pageable pageable);
}
