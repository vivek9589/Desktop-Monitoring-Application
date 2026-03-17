package com.braininventory.monitoring.screenshot.monitor.agent.agent.activity;

import lombok.extern.slf4j.Slf4j;




import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ActivityState {

    private static final AtomicInteger keyboardCount = new AtomicInteger(0);
    private static final AtomicInteger mouseCount = new AtomicInteger(0);

    private static volatile long lastActivityTime = System.currentTimeMillis();
    private static volatile Long idleStartTime = null;

    private static final long IDLE_THRESHOLD = 60 * 1000; // 1 min

    public static void recordKeyboard() {
        keyboardCount.incrementAndGet();
        lastActivityTime = System.currentTimeMillis();
    }

    public static void recordMouse() {
        mouseCount.incrementAndGet();
        lastActivityTime = System.currentTimeMillis();
    }

    public static boolean isIdle() {
        boolean idle = (System.currentTimeMillis() - lastActivityTime) > IDLE_THRESHOLD;

        if (idle && idleStartTime == null) {
            idleStartTime = System.currentTimeMillis();
            log.info("User became IDLE");
        }

        if (!idle) {
            idleStartTime = null;
        }

        return idle;
    }

    // 🔥 IMPORTANT: Atomic fetch + reset
    public static int getAndResetKeyboard() {
        return keyboardCount.getAndSet(0);
    }

    public static int getAndResetMouse() {
        return mouseCount.getAndSet(0);
    }
}