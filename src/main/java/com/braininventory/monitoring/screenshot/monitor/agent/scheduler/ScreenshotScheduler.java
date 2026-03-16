package com.braininventory.monitoring.screenshot.monitor.agent.scheduler;


import com.braininventory.monitoring.screenshot.monitor.agent.capture.ScreenCaptureService;
import com.braininventory.monitoring.screenshot.monitor.agent.config.AppConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScreenshotScheduler {

    private final ScreenCaptureService captureService;
    private final AppConfig config;

    public ScreenshotScheduler(ScreenCaptureService captureService,
                               AppConfig config) {
        this.captureService = captureService;
        this.config = config;
    }

    @Scheduled(fixedRateString = "#{${screenshot.interval.minutes} * 60000}")
    public void takeScreenshot() {

        captureService.captureScreen();

    }
}