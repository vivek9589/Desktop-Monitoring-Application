package com.braininventory.monitoring.agent.activity;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeyboardTracker implements NativeKeyListener {

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        ActivityState.recordKeyboard();
        log.info("Key Pressed");
    }
}