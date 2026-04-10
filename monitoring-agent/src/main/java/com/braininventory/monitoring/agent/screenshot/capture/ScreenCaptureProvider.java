package com.braininventory.monitoring.agent.screenshot.capture;

import com.braininventory.monitoring.common.exception.AgentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.awt.*;
import java.awt.image.BufferedImage;

@Slf4j
@Component
public class ScreenCaptureProvider {

    public BufferedImage capture() {
        if (GraphicsEnvironment.isHeadless()) {
            throw new AgentException("Headless environment detected. Cannot capture screen.");
        }

        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            return robot.createScreenCapture(screenRect);
        } catch (Exception e) {
            log.error("Failed to capture screen", e);
            throw new AgentException("Screen capture failed: " + e.getMessage());
        }
    }
}
