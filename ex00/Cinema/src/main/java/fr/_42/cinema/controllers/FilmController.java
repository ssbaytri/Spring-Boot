package fr._42.cinema.controllers;

import fr._42.cinema.models.Film;
import fr._42.cinema.models.UploadContext;
import fr._42.cinema.models.UploadedFile;
import fr._42.cinema.security.CinemaUserDetails;
import fr._42.cinema.services.FileStorageService;
import fr._42.cinema.services.FilmService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin/panel/films")
public class FilmController {

    private final FilmService filmService;
    private final FileStorageService fileStorageService;

    public FilmController(FilmService filmService, FileStorageService fileStorageService) {
        this.filmService = filmService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public String listFilms(Model model) {
        model.addAttribute("films", filmService.findAll());
        return "admin/films";
    }

    @PostMapping
    public String createFilm(
            @RequestParam("title") String title,
            @RequestParam("releaseYear") Integer releaseYear,
            @RequestParam("ageRestriction") Integer ageRestriction,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "poster", required = false) MultipartFile poster,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        String posterUrl = null;
        if (poster != null && !poster.isEmpty()) {
            CinemaUserDetails admin = (CinemaUserDetails) authentication.getPrincipal();
            UploadedFile stored = fileStorageService.store(poster, UploadContext.POSTER, admin.getUser());
            posterUrl = "/images/" + stored.getStoredName();
        }

        Film film = new Film(title, releaseYear, ageRestriction, description, posterUrl);
        filmService.save(film);

        redirectAttributes.addFlashAttribute("success", "Film created successfully");
        return "redirect:/admin/panel/films";
    }

}