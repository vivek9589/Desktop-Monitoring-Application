package com.braininventory.monitoring.agent.apptracking.detector;

import org.springframework.stereotype.Component;

@Component
public class OSDetector {

    public static String getOS() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) return "windows";
        if (os.contains("mac")) return "mac";
        if (os.contains("nix") || os.contains("nux")) return "linux";

        return "unknown";
    }
}
