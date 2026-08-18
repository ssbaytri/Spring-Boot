package fr._42.cinema.controllers;

import fr._42.cinema.dto.SessionMapper;
import fr._42.cinema.dto.SessionSearchResultDTO;
import fr._42.cinema.dto.SessionSearchResponseDTO;
import fr._42.cinema.models.Session;
import fr._42.cinema.services.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PublicSessionController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final SessionService sessionService;
    private final SessionMapper sessionMapper;

    public PublicSessionController(SessionService sessionService, SessionMapper sessionMapper) {
        this.sessionService = sessionService;
        this.sessionMapper = sessionMapper;
    }

    @GetMapping("/session/search")
    public String searchPage() {
        return "session/search";
    }

    @GetMapping("/sessions/search")
    @ResponseBody
    public SessionSearchResponseDTO search(
            @RequestParam(value = "filmName", required = false, defaultValue = "") String filmName) {
        List<Session> sessions = filmName.isBlank()
                ? sessionService.findAll()
                : sessionService.findByFilmTitle(filmName);
        List<SessionSearchResultDTO> results = sessions.stream()
                .map(sessionMapper::toSearchResultDTO)
                .collect(Collectors.toList());
        return new SessionSearchResponseDTO(results);
    }

    @GetMapping("/sessions/{id}")
    public String sessionDetail(@PathVariable("id") Long id, Model model) {
        Session session = sessionService.findById(id);
        model.addAttribute("session", session);
        model.addAttribute("formatter", FORMATTER);
        return "session/session-detail";
    }
}