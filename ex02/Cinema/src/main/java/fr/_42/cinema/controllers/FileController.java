package fr._42.cinema.controllers;

import fr._42.cinema.models.UploadContext;
import fr._42.cinema.models.UploadedFile;
import fr._42.cinema.security.CinemaUserDetails;
import fr._42.cinema.services.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.regex.Pattern;

@RestController
public class FileController {

    private static final Pattern CHAT_RETURN_TO = Pattern.compile("^/films/\\d+/chat$");

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/images/{storedName}")
    public ResponseEntity<Resource> serveFile(@PathVariable String storedName) throws IOException {
        UploadedFile metadata = fileStorageService.findByStoredName(storedName)
                .orElse(null);
        if (metadata == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = fileStorageService.loadAsResource(storedName);
        MediaType mediaType = metadata.getMimeType() != null
                ? MediaType.parseMediaType(metadata.getMimeType())
                : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + metadata.getStoredName() + "\"")
                .body(resource);
    }

    @PostMapping("/images")
    public RedirectView uploadAvatar(@RequestParam("avatar") MultipartFile avatar,
                                     @RequestParam(value = "returnTo", required = false) String returnTo,
                                     Authentication authentication)
            throws IOException {
        CinemaUserDetails userDetails = (CinemaUserDetails) authentication.getPrincipal();
        fileStorageService.store(avatar, UploadContext.AVATAR, userDetails.getUser());
        String target = returnTo != null && CHAT_RETURN_TO.matcher(returnTo).matches()
                ? returnTo
                : "/profile";
        return new RedirectView(target, true);
    }
}