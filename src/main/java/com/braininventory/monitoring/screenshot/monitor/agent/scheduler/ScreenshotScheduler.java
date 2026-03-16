package com.braininventory.monitoring.screenshot.monitor.agent.scheduler;


import com.braininventory.monitoring.screenshot.monitor.agent.capture.ScreenCaptureService;

import com.braininventory.monitoring.screenshot.monitor.agent.config.AgentConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScreenshotScheduler {

    private final ScreenCaptureService captureService;
    private final AgentConfig agentConfig;

    public ScreenshotScheduler(ScreenCaptureService captureService,
                               AgentConfig agentConfig) {
        this.captureService = captureService;
        this.agentConfig = agentConfig;
    }

    @Scheduled(fixedRateString = "#{${screenshot.interval.minutes} * 60000}")
    public void takeScreenshot() {

        captureService.captureScreen();

    }
}