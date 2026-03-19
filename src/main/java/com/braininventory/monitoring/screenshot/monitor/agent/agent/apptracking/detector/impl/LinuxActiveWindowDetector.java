package com.braininventory.monitoring.screenshot.monitor.agent.agent.apptracking.detector.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.agent.apptracking.detector.ActiveWindowDetector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;



// Note :- Linux requires to install
// sudo apt install xdotool
@Component
@ConditionalOnProperty(name = "os.type", havingValue = "linux")
public class LinuxActiveWindowDetector implements ActiveWindowDetector {

    @Override
    public String getActiveApp() {
        return runCommand("xdotool getactivewindow getwindowpid");
    }

    @Override
    public String getWindowTitle() {
        return runCommand("xdotool getactivewindow getwindowname");
    }

    private String runCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            return new String(process.getInputStream().readAllBytes()).trim();
        } catch (Exception e) {
            return "unknown";
        }
    }
}