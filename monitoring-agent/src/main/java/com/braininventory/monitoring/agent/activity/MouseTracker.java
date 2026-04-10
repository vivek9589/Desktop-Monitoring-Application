package com.braininventory.monitoring.agent.activity;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MouseTracker implements NativeMouseListener {

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        ActivityState.recordMouse();
        log.info("Mouse Clicked");
    }
}
