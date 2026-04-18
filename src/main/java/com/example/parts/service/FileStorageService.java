package com.example.parts.service;

import com.example.parts.config.PartsProperties;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);

    private final Path uploadPath;

    public FileStorageService(PartsProperties partsProperties) {
        this.uploadPath = Path.of(partsProperties.getUploadDir()).toAbsolutePath().normalize();
    }

    @PostConstruct
    void init() throws IOException {
        Files.createDirectories(uploadPath);
        log.debug("Upload directory is ready at {}", uploadPath);
    }

    public List<String> storeImages(MultipartFile[] images) {
        List<String> storedImages = new ArrayList<>();
        if (images == null) {
            return storedImages;
        }

        for (MultipartFile image : images) {
            String storedFilename = storeImage(image);
            if (storedFilename != null) {
                storedImages.add(storedFilename);
            }
        }

        return storedImages;
    }

    public String storeImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null;
        }

        String contentType = image.getContentType();
        if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new IllegalArgumentException("ატვირთე მხოლოდ სურათის ფაილი");
        }

        String originalFilename = StringUtils.cleanPath(image.getOriginalFilename());
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }

        String fileName = UUID.randomUUID() + extension;
        Path destination = uploadPath.resolve(fileName);

        try (InputStream inputStream = image.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            log.info("Stored uploaded file {}", destination.getFileName());
            return fileName;
        } catch (IOException e) {
            throw new IllegalStateException("ფაილის შენახვა ვერ მოხერხდა", e);
        }
    }

    public void deleteImages(Iterable<String> filenames) {
        if (filenames == null) {
            return;
        }

        for (String filename : filenames) {
            deleteImage(filename);
        }
    }

    public void deleteImage(String filename) {
        if (filename == null || filename.isBlank()) {
            return;
        }

        Path destination = uploadPath.resolve(filename).normalize();
        if (!destination.startsWith(uploadPath)) {
            log.warn("Refused to delete file outside upload directory: {}", filename);
            return;
        }

        try {
            if (Files.deleteIfExists(destination)) {
                log.info("Deleted uploaded file {}", destination.getFileName());
            }
        } catch (IOException e) {
            log.warn("Could not delete uploaded file {}", destination.getFileName(), e);
        }
    }
}
