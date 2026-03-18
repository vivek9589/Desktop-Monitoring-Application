package com.braininventory.monitoring.screenshot.monitor.agent.agent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ScreenshotConfig {

    @Value("${screenshot.interval.minutes}")
    private int screenshotIntervalMinutes;

    @Value("${screenshot.storage.path}")
    private String storagePath;

    @Value("${agent.id}")
    private String agentId;

    public int getScreenshotIntervalMinutes() {
        return screenshotIntervalMinutes;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getAgentId() {
        return agentId;
    }
}