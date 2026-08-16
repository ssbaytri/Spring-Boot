package fr._42.cinema.services;

import fr._42.cinema.models.UploadContext;
import fr._42.cinema.models.UploadedFile;
import fr._42.cinema.models.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface FileStorageService {

    /**
     * Saves the given multipart file to disk and records it in uploaded_file.
     *
     * @param file    the uploaded file
     * @param context what this upload is for (AVATAR, CHAT, POSTER)
     * @param owner   the user responsible for the upload, or null when there is no
     *                meaningful owner (e.g. context has none applicable)
     */
    UploadedFile store(MultipartFile file, UploadContext context, User owner) throws IOException;

    Optional<UploadedFile> findByStoredName(String storedName);

    Resource loadAsResource(String storedName) throws IOException;
}