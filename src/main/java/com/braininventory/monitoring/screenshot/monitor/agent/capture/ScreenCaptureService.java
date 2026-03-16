package com.braininventory.monitoring.screenshot.monitor.agent.capture;


import com.braininventory.monitoring.screenshot.monitor.agent.config.AppConfig;
import com.braininventory.monitoring.screenshot.monitor.agent.storage.ScreenshotStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import java.time.LocalDateTime;

@Service
public class ScreenCaptureService {

    private static final Logger logger = LoggerFactory.getLogger(ScreenCaptureService.class);

    private final AppConfig config;
    private final ScreenshotStorageService storageService;
    private final RestTemplate restTemplate;

    @Value("${backend.api.screenshot-upload-url}")
    private String backendUrl;

    public ScreenCaptureService(AppConfig config,
                                ScreenshotStorageService storageService,
                                RestTemplate restTemplate) {
        this.config = config;
        this.storageService = storageService;
        this.restTemplate = restTemplate;
    }

    public void captureScreen() {
        if (GraphicsEnvironment.isHeadless()) {
            logger.warn("Cannot capture screen in headless environment");
            return;
        }

        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screen = robot.createScreenCapture(screenRect);

            File output = storageService.createScreenshotFile(config.getStoragePath());
            ImageIO.write(screen, "png", output);

            logger.info("Screenshot saved locally: {}", output.getAbsolutePath());

            // 🔥 Automatically upload to backend API
            uploadToBackend(output);

        } catch (Exception e) {
            logger.error("Error capturing screenshot", e);
        }
    }

    private void uploadToBackend(File screenshotFile) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(screenshotFile));
            body.add("agentId", config.getAgentId());
            body.add("timestamp", LocalDateTime.now().toString());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(backendUrl, requestEntity, String.class);

            logger.info("Upload response: {}", response.getBody());

        } catch (Exception e) {
            logger.error("Failed to upload screenshot", e);
        }
    }


}