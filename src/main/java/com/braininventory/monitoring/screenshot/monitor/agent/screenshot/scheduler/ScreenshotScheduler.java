package com.braininventory.monitoring.screenshot.monitor.agent.screenshot.scheduler;


import com.braininventory.monitoring.screenshot.monitor.agent.screenshot.capture.ScreenCaptureService;

import com.braininventory.monitoring.screenshot.monitor.agent.common.config.AgentConfig;
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