package com.braininventory.monitoring.screenshot.monitor.agent.agent.website.state;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.ZoneId;

/**
 * Holds current website session state inside agent.
 */
@Data
public class WebsiteSession {

    private String url;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public long getDurationSeconds() {
        if (startTime != null && endTime != null) {
            return Duration.between(startTime, endTime).getSeconds();
        }
        return 0;
    }

    public static LocalDateTime nowIST() {
        return LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
    }
}