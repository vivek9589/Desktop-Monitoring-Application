package com.braininventory.monitoring.agent.activity;


import com.github.kwhat.jnativehook.GlobalScreen;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrackerInitializer {

    @PostConstruct
    public void init() {
        try {
            GlobalScreen.registerNativeHook();

            GlobalScreen.addNativeKeyListener(new KeyboardTracker());
            GlobalScreen.addNativeMouseListener(new MouseTracker());

            log.info("Activity Trackers initialized successfully");

        } catch (Exception e) {
            log.error("Failed to initialize trackers", e);
        }
    }
}