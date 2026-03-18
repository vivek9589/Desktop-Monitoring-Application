package com.braininventory.monitoring.screenshot.monitor.agent.agent.screenshot.scheduler;


import com.braininventory.monitoring.screenshot.monitor.agent.agent.client.ScreenshotUploadClient;
import com.braininventory.monitoring.screenshot.monitor.agent.agent.config.AgentProperties;
import com.braininventory.monitoring.screenshot.monitor.agent.agent.screenshot.capture.ScreenCaptureProvider;

import com.braininventory.monitoring.screenshot.monitor.agent.agent.screenshot.storage.ScreenshotStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


@Component
@RequiredArgsConstructor
@Slf4j
public class ScreenshotJob {

    private final ScreenCaptureProvider captureProvider;
    private final ScreenshotStorageService storageService;
    private final ScreenshotUploadClient uploadClient;
    private final AgentProperties agentProperties;

    @Scheduled(fixedRateString = "#{${screenshot.interval.minutes} * 60000}")
    public void execute() {

        BufferedImage image = captureProvider.capture();

        if (image == null) {
            return;
        }

        try {
            File file = storageService.createScreenshotFile(agentProperties.getStoragePath());
            ImageIO.write(image, "png", file);

            log.info("Screenshot saved: {}", file.getAbsolutePath());

            uploadClient.upload(file, agentProperties.getAgentId());

        } catch (Exception e) {
            log.error("Error processing screenshot scheduler", e);
        }
    }
}