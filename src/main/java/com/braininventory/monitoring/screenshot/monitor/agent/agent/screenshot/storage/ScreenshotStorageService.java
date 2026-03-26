package com.braininventory.monitoring.screenshot.monitor.agent.agent.screenshot.storage;


import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class ScreenshotStorageService {

    public File createScreenshotFile(String basePath, String agentId) {
        File directory = new File(basePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String filename = String.format("screenshot_%s_%s.png", agentId, timestamp);
        return new File(directory, filename);
    }
}
