package fr._42.cinema.controllers;

import fr._42.cinema.dto.ChatMessageInboundDTO;
import fr._42.cinema.dto.ChatMessageOutboundDTO;
import fr._42.cinema.models.ChatMessage;
import fr._42.cinema.models.Film;
import fr._42.cinema.repositories.ChatMessageRepository;
import fr._42.cinema.security.CinemaUserDetails;
import fr._42.cinema.services.FilmService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ChatStompController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ChatMessageRepository chatMessageRepository;
    private final FilmService filmService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatStompController(ChatMessageRepository chatMessageRepository, FilmService filmService,
                               SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.filmService = filmService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/films/{filmId}/chat/send")
    public void sendMessage(@DestinationVariable("filmId") Long filmId,
                            ChatMessageInboundDTO inbound,
                            Principal principal) {
        CinemaUserDetails userDetails = extractUser(principal);
        if (userDetails == null || inbound.getContent() == null || inbound.getContent().isBlank()) {
            return;
        }

        Film film = filmService.findById(filmId);
        if (film == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        ChatMessage message = new ChatMessage(film, userDetails.getUser(), inbound.getContent(), now);
        chatMessageRepository.save(message);

        ChatMessageOutboundDTO outbound = new ChatMessageOutboundDTO(
                userDetails.getUser().getId(),
                userDetails.getUser().getFirstName(),
                userDetails.getUser().getLastName(),
                inbound.getContent(),
                now.format(FORMATTER));

        messagingTemplate.convertAndSend("/films/" + filmId + "/chat/messages", outbound);
    }

    private CinemaUserDetails extractUser(Principal principal) {
        Object candidate = principal;
        if (principal instanceof Authentication authentication) {
            candidate = authentication.getPrincipal();
        }
        return candidate instanceof CinemaUserDetails userDetails ? userDetails : null;
    }
}