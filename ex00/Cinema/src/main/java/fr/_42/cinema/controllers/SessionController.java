package fr._42.cinema.controllers;

import fr._42.cinema.models.Hall;
import fr._42.cinema.models.Film;
import fr._42.cinema.models.Session;
import fr._42.cinema.services.HallService;
import fr._42.cinema.services.FilmService;
import fr._42.cinema.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin/panel/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final FilmService filmService;
    private final HallService hallService;

    @Autowired
    public SessionController(SessionService sessionService, FilmService filmService, HallService hallService) {
        this.sessionService = sessionService;
        this.filmService = filmService;
        this.hallService = hallService;
    }

    @GetMapping
    public String listSessions(CsrfToken csrfToken, Model model) {
        model.addAttribute("_csrf", csrfToken);
        model.addAttribute("sessions", sessionService.findAll());
        model.addAttribute("films", filmService.findAll());
        model.addAttribute("halls", hallService.findAll());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        model.addAttribute("formatter", formatter);
        return "admin/sessions";
    }

    @PostMapping
    public String createSession(
            @RequestParam("filmId") Long filmId,
            @RequestParam("hallId") Long hallId,
            @RequestParam("dateTime") @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime dateTime,
            @RequestParam("ticketPrice") BigDecimal ticketPrice,
            RedirectAttributes redirectAttributes
    ) {
        Film film = filmService.findById(filmId);
        Hall hall = hallService.findById(hallId);

        Session session = new Session(film, hall, dateTime, ticketPrice);
        sessionService.save(session);

        redirectAttributes.addFlashAttribute("success", "Session created successfully");
        return "redirect:/admin/panel/sessions";
    }

}