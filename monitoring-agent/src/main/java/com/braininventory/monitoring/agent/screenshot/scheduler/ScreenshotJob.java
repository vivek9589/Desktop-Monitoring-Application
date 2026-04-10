package com.braininventory.monitoring.agent.screenshot.scheduler;


import com.braininventory.monitoring.agent.client.ScreenshotUploadClient;
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

    @Value("${agent.screenshot.storage-path}")
    private String storagePath;

    @Value("${agent.id}")
    private String agentId;

    @Scheduled(fixedRateString = "#{${agent.screenshot.interval-minutes} * 60000}")
    public void execute() {
        try {
            BufferedImage image = captureProvider.capture();
            File file = storageService.createScreenshotFile(storagePath, agentId);
            ImageIO.write(image, "png", file);

            log.info("Screenshot saved: {}", file.getAbsolutePath());
            uploadClient.upload(file, agentId);

        } catch (AgentException ex) {
            log.warn("Screenshot skipped: {}", ex.getMessage());
        } catch (Exception e) {
            log.error("Error processing screenshot scheduler", e);
        }
    }
}
