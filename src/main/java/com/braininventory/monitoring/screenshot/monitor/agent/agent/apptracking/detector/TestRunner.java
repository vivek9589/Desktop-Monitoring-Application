package com.braininventory.monitoring.screenshot.monitor.agent.agent.apptracking.detector;

import com.braininventory.monitoring.screenshot.monitor.agent.agent.apptracking.detector.impl.WindowsActiveWindowDetector;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {

    private final WindowsActiveWindowDetector detector;

    @Override
    public void run(String... args) {

        while (true) {
            String app = detector.getActiveApp();
            String title = detector.getWindowTitle();

            System.out.println("App: " + app + " | Title: " + title);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {}
        }
    }
}
