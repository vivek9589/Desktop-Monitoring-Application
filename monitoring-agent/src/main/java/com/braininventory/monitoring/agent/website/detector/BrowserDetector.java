package com.braininventory.monitoring.agent.website.detector;

import org.springframework.stereotype.Component;
import java.util.Set;

/**
 * Detects whether a given process name belongs to a supported browser.
 */
@Component
public class BrowserDetector {

    private static final Set<String> SUPPORTED_BROWSERS = Set.of(
            "chrome.exe", "msedge.exe", "firefox.exe"
    );

    public boolean isBrowser(String processName) {
        if (processName == null || processName.isBlank()) return false;
        return SUPPORTED_BROWSERS.contains(processName.toLowerCase().trim());
    }
}