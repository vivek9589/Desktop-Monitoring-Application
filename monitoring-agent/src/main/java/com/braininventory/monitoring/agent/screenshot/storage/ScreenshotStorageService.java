package com.braininventory.monitoring.agent.screenshot.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class ScreenshotStorageService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public File createScreenshotFile(String basePath, String userId) {
        try {
            // 1. Create a sub-directory for the specific user
            // Path: basePath/screenshots/96634b39-f999.../
            Path userDirectory = Paths.get(basePath, "screenshots", userId);

            if (!Files.exists(userDirectory)) {
                Files.createDirectories(userDirectory);
            }

            // 2. Generate timestamp
            String timestamp = LocalDateTime.now().format(FORMATTER);

            // 3. Create filename - we keep it simple since it's already in the user's folder
            String filename = String.format("sc_%s.png", timestamp);

            File finalFile = userDirectory.resolve(filename).toFile();

            log.debug("Prepared screenshot path: {}", finalFile.getAbsolutePath());
            return finalFile;

        } catch (IOException e) {
            log.error("Could not create screenshot directory at {}", basePath, e);
            // Fallback to temporary directory if configured path fails
            return createFallbackFile(userId);
        }
    }

    private File createFallbackFile(String userId) {
        String tempDir = System.getProperty("java.io.tmpdir");
        String timestamp = LocalDateTime.now().format(FORMATTER);
        return new File(tempDir, "fallback_sc_" + userId + "_" + timestamp + ".png");
    }
}