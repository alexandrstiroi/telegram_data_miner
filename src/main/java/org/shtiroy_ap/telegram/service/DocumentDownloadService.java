package org.shtiroy_ap.telegram.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.model.Document;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class DocumentDownloadService {
    private final Logger log = LogManager.getLogger(DocumentDownloadService.class.getName());
    /**
     * Скачивает файл по URL и сохраняет его во временную директорию.
     */
    public File downloadToFile(Document document) throws IOException {
        URL url = new URL(document.getUrl().replaceAll("http://","https://"));
        String safeFileName = document.getTitle().replaceAll("[^a-zA-Z0-9._-]", "_");
        Path tempFile = Files.createTempFile("tender_", "_" + safeFileName);

        try (InputStream in = url.openStream()) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        log.info("Скачан файл: {}", tempFile);
        return tempFile.toFile();
    }

    /**
     * Удаляет временный файл.
     */
    public void deleteFile(File file) {
        if (file.delete()) {
            log.info("Удалён временный файл: {}", file.getAbsolutePath());
        } else {
            log.warn("Не удалось удалить файл: {}", file.getAbsolutePath());
        }
    }
}
