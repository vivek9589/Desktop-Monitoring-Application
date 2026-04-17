package com.braininventory.monitoring.agent.screenshot.scheduler;

import com.braininventory.monitoring.agent.client.ScreenshotUploadClient;
import com.braininventory.monitoring.agent.config.AuthContext;
import com.braininventory.monitoring.agent.config.TokenManager;
import com.braininventory.monitoring.agent.screenshot.capture.ScreenCaptureProvider;
import com.braininventory.monitoring.agent.screenshot.storage.ScreenshotStorageService;
import com.braininventory.monitoring.common.exception.AgentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScreenshotJob {

    private final ScreenCaptureProvider captureProvider;
    private final ScreenshotStorageService storageService;
    private final ScreenshotUploadClient uploadClient;
    private final AuthContext authContext; // Inject AuthContext
    private final TokenManager tokenManager; // Inject TokenManager

    @Value("${agent.screenshot.storage-path}")
    private String storagePath;

    @Scheduled(fixedRateString = "#{${agent.screenshot.interval-minutes} * 60000}")
    public void execute() {
        // 1. Pre-check: Don't take screenshots if user is logged out
        if (!tokenManager.isAuthenticated()) {
            log.trace("Screenshot skipped: User not authenticated.");
            return;
        }

        try {
            // 2. Get dynamic IDs from the token
            String userId = authContext.getUserId();
            String orgId = authContext.getOrganizationId();

            // 3. Capture and Save using dynamic User ID
            BufferedImage image = captureProvider.capture();
            File file = storageService.createScreenshotFile(storagePath, userId);
            ImageIO.write(image, "png", file);

            log.info("Screenshot saved for user {}: {}", userId, file.getAbsolutePath());

            // 4. Upload to backend
            // Note: If your uploadClient.upload() doesn't support orgId yet,
            // you should update that method signature as well.
            uploadClient.upload(file, userId);

        } catch (AgentException ex) {
            log.warn("Screenshot skipped: {}", ex.getMessage());
        } catch (Exception e) {
            log.error("Error processing screenshot scheduler", e);
        }
    }
}