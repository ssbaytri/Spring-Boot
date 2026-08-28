package fr._42.cinema.controllers;

import fr._42.cinema.dto.LoginHistoryEntry;
import fr._42.cinema.dto.UploadedFileInfo;
import fr._42.cinema.models.UploadContext;
import fr._42.cinema.models.UploadedFile;
import fr._42.cinema.security.CinemaUserDetails;
import fr._42.cinema.services.FileStorageService;
import fr._42.cinema.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final UserService userService;
    private final FileStorageService fileStorageService;

    public ProfileController(UserService userService, FileStorageService fileStorageService) {
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public String profile(Authentication authentication, CsrfToken csrfToken, Model model) {
        CinemaUserDetails userDetails = (CinemaUserDetails) authentication.getPrincipal();

        List<LoginHistoryEntry> logHistory = userService.getAuthenticationLogs(userDetails.getUser())
                .stream()
                .map(log -> new LoginHistoryEntry(
                        log.getAuthenticatedAt().format(DATE_FORMATTER),
                        log.getAuthenticatedAt().format(TIME_FORMATTER),
                        log.getIpAddress()))
                .toList();

        List<UploadedFile> files = fileStorageService.findAllByOwnerAndContext(
                userDetails.getUser(), UploadContext.AVATAR);
        List<UploadedFileInfo> uploadedFiles = files.stream()
                .map(file -> new UploadedFileInfo(
                        file.getOriginalName(),
                        file.getStoredName(),
                        formatSize(file.getSizeBytes()),
                        file.getMimeType()))
                .toList();

        String avatarUrl = fileStorageService.findLatestByOwnerAndContext(userDetails.getUser(), UploadContext.AVATAR)
                .map(file -> "/images/" + file.getStoredName())
                .orElse("/imgs/pfp.png");

        model.addAttribute("_csrf", csrfToken);
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("logHistory", logHistory);
        model.addAttribute("uploadedFiles", uploadedFiles);
        model.addAttribute("avatarUrl", avatarUrl);
        return "profile";
    }

    private String formatSize(Long sizeBytes) {
        if (sizeBytes == null) {
            return "";
        }
        if (sizeBytes < 1024) {
            return sizeBytes + " B";
        }
        double kb = sizeBytes / 1024.0;
        if (kb < 1024) {
            return String.format("%.1f KB", kb);
        }
        return String.format("%.1f MB", kb / 1024.0);
    }
}