package fr._42.cinema.controllers;

import fr._42.cinema.models.ChatMessage;
import fr._42.cinema.models.Film;
import fr._42.cinema.models.UploadContext;
import fr._42.cinema.models.UploadedFile;
import fr._42.cinema.models.User;
import fr._42.cinema.repositories.ChatMessageRepository;
import fr._42.cinema.security.CinemaUserDetails;
import fr._42.cinema.services.FileStorageService;
import fr._42.cinema.services.FilmService;
import fr._42.cinema.services.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Controller
public class FilmChatController {

    private static final int LAST_MESSAGES_LIMIT = 20;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final FilmService filmService;
    private final ChatMessageRepository chatMessageRepository;
    private final FileStorageService fileStorageService;
    private final UserService userService;

    public FilmChatController(FilmService filmService,
                              ChatMessageRepository chatMessageRepository,
                              FileStorageService fileStorageService,
                              UserService userService) {
        this.filmService = filmService;
        this.chatMessageRepository = chatMessageRepository;
        this.fileStorageService = fileStorageService;
        this.userService = userService;
    }

    @GetMapping("/films/{filmId}/chat")
    public String chatPage(@PathVariable("filmId") Long filmId,
                           Authentication authentication,
                           CsrfToken csrfToken,
                           Model model) {
        Film film = filmService.findById(filmId);
        if (film == null) {
            return "redirect:/session/search";
        }

        List<ChatMessage> messages = chatMessageRepository.findByFilmIdOrderBySentAtDesc(
                filmId, PageRequest.of(0, LAST_MESSAGES_LIMIT));
        Collections.reverse(messages);

        CinemaUserDetails userDetails = (CinemaUserDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();

        model.addAttribute("_csrf", csrfToken);
        model.addAttribute("film", film);
        model.addAttribute("messages", messages);
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("formatter", FORMATTER);
        model.addAttribute("myAvatar",
                fileStorageService.findLatestByOwnerAndContext(currentUser, UploadContext.AVATAR).orElse(null));
        model.addAttribute("myImages",
                fileStorageService.findAllByOwnerAndContext(currentUser, UploadContext.AVATAR));
        model.addAttribute("authenticationLogs", userService.getAuthenticationLogs(currentUser));
        return "films/chat";
    }
}