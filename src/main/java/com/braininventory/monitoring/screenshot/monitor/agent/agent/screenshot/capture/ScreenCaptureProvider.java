package com.braininventory.monitoring.screenshot.monitor.agent.agent.screenshot.capture;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.awt.*;
import java.awt.image.BufferedImage;


@Slf4j
@Component
public class ScreenCaptureProvider {

    public BufferedImage capture() {
        if (GraphicsEnvironment.isHeadless()) {
            log.warn("Headless environment detected. Skipping capture.");
            return null;
        }

        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            return robot.createScreenCapture(screenRect);
        } catch (Exception e) {
            log.error("Failed to capture screen", e);
            return null;
        }
    }
}