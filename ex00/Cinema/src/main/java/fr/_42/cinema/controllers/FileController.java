package fr._42.cinema.controllers;

import fr._42.cinema.models.UploadedFile;
import fr._42.cinema.services.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class FileController {

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
}