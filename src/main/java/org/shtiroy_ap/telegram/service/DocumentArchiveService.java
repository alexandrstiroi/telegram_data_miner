package org.shtiroy_ap.telegram.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.model.Document;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DocumentArchiveService {
    private final Logger log = LogManager.getLogger(DocumentArchiveService.class.getName());

    public File downloadAndZipDocuments(List<Document> documents) throws IOException {
        Path tempDir = Files.createTempDirectory("tender_docs_");
        List<Path> downloadedFiles = new ArrayList<>();

        for (Document doc : documents) {
            URL url = new URL(doc.getUrl().replaceAll("http://","https://"));
            String safeName = doc.getTitle().replaceAll("[^a-zA-Z0-9._-]", "_");
            Path filePath = tempDir.resolve(safeName);
            try (InputStream in = url.openStream()) {
                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
                downloadedFiles.add(filePath);
                log.info("Скачан файл: {}", filePath);
            }
        }

        // Создаем ZIP
        Path zipPath = Files.createTempFile("tender_docs_", ".zip");
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
            for (Path file : downloadedFiles) {
                try (InputStream fis = Files.newInputStream(file)) {
                    ZipEntry entry = new ZipEntry(file.getFileName().toString());
                    zipOut.putNextEntry(entry);
                    fis.transferTo(zipOut);
                    zipOut.closeEntry();
                }
            }
        }

        // Очистка исходных файлов
        for (Path file : downloadedFiles) {
            Files.deleteIfExists(file);
        }
        Files.deleteIfExists(tempDir);

        log.info("Создан архив: {}", zipPath);
        return zipPath.toFile();
    }

    public void deleteFile(File file) {
        if (file != null && file.exists() && file.delete()) {
            log.info("Удалён архив: {}", file.getAbsolutePath());
        }
    }
}
