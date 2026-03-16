package com.braininventory.monitoring.screenshot.monitor.agent.storage;


import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ScreenshotStorageService {

    public File createScreenshotFile(String basePath) {

        File directory = new File(basePath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String timestamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        return new File(directory, "screenshot_" + timestamp + ".png");
    }
}