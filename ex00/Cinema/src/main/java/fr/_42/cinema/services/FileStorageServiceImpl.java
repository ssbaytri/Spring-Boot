package fr._42.cinema.services;

import fr._42.cinema.models.UploadContext;
import fr._42.cinema.models.UploadedFile;
import fr._42.cinema.models.User;
import fr._42.cinema.repositories.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final UploadedFileRepository uploadedFileRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    public FileStorageServiceImpl(UploadedFileRepository uploadedFileRepository) {
        this.uploadedFileRepository = uploadedFileRepository;
    }

    @Override
    public UploadedFile store(MultipartFile file, UploadContext context, User owner) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalName = file.getOriginalFilename();
        String storedName = UUID.randomUUID() + extractExtension(originalName);

        Path targetPath = uploadPath.resolve(storedName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        UploadedFile uploadedFile = new UploadedFile(
                owner,
                context,
                originalName,
                storedName,
                file.getSize(),
                file.getContentType(),
                LocalDateTime.now()
        );

        return uploadedFileRepository.save(uploadedFile);
    }

    @Override
    public Optional<UploadedFile> findByStoredName(String storedName) {
        return uploadedFileRepository.findByStoredName(storedName);
    }

    @Override
    public Resource loadAsResource(String storedName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(storedName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException("File not found: " + storedName);
        }

        return resource;
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }
}