package com.braininventory.monitoring.screenshot.monitor.agent.agent.apptracking.state;


import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Data
public class AppTrackingState {

    private static String currentApp = null;
    private static String currentTitle = null;
    private static LocalDateTime startTime = null;

    public static boolean isFirstRun() {
        return currentApp == null;
    }

    public static void startSession(String app, String title) {
        currentApp = app;
        currentTitle = title;
        startTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
    }

    public static boolean isAppChanged(String newApp) {
        return currentApp != null && !currentApp.equals(newApp);
    }

    public static void clear() {
        currentApp = null;
        currentTitle = null;
        startTime = null;
    }

    public static String getCurrentApp() {
        return currentApp;
    }

    public static String getCurrentTitle() {
        return currentTitle;
    }

    public static LocalDateTime   getStartTime() {
        return startTime;
    }
}