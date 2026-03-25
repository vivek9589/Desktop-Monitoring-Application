package com.braininventory.monitoring.screenshot.monitor.agent.agent.apptracking.detector.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.agent.apptracking.detector.ActiveWindowDetector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@ConditionalOnProperty(name = "os.type", havingValue = "mac")
public class MacActiveWindowDetector implements ActiveWindowDetector {

    @Override
    public String getActiveApp() {
        return runCommand("osascript -e 'tell application \"System Events\" to get name of first application process whose frontmost is true'");
    }

    @Override
    public String getWindowTitle() {
        return runCommand("osascript -e 'tell application \"System Events\" to get name of front window of (first application process whose frontmost is true)'");
    }

    private String runCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);

            String result = new String(process.getInputStream().readAllBytes()).trim();

            process.destroy();

            log.debug("Command executed: {} -> {}", command, result);

            return result.isEmpty() ? "unknown" : result;

        } catch (Exception e) {
            log.error("Command execution failed: {}", command, e);
            return "unknown";
        }
    }
}