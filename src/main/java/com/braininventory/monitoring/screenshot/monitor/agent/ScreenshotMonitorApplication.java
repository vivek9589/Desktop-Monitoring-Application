package com.braininventory.monitoring.screenshot.monitor.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScreenshotMonitorApplication {

    public static void main(String[] args) {

        // Required for screenshot + native hooks
        System.setProperty("java.awt.headless", "false");

        SpringApplication.run(ScreenshotMonitorApplication.class, args);
    }
}