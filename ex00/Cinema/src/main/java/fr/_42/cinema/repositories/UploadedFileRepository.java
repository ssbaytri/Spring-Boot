package fr._42.cinema.repositories;

import fr._42.cinema.models.UploadContext;
import fr._42.cinema.models.UploadedFile;
import fr._42.cinema.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {

    Optional<UploadedFile> findByStoredName(String storedName);

    List<UploadedFile> findAllByOwnerAndContext(User owner, UploadContext context);

    Optional<UploadedFile> findFirstByOwnerAndContextOrderByUploadedAtDesc(User owner, UploadContext context);
}
