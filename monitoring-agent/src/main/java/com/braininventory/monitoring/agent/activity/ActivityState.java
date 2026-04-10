package com.braininventory.monitoring.agent.activity;

import com.braininventory.monitoring.common.dto.request.IdleSessionDto;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ActivityState {

    // ================= ACTIVITY COUNTERS =================
    private static final AtomicInteger keyboardCount = new AtomicInteger(0);
    private static final AtomicInteger mouseCount = new AtomicInteger(0);

    private static volatile long lastActivityTime = System.currentTimeMillis();

    private static final long IDLE_THRESHOLD = 60 * 1000; // 1 min

    // ================= IDLE STATE =================
    private static volatile boolean currentlyIdle = false;
    private static volatile LocalDateTime idleStartTime = null;

    // Thread-safe list handling via synchronization
    private static final List<IdleSessionDto> idleSessions = new ArrayList<>();

    // ================= ACTIVITY EVENTS =================

    public static void recordKeyboard() {
        keyboardCount.incrementAndGet();
        onUserActivity();
    }

    public static void recordMouse() {
        mouseCount.incrementAndGet();
        onUserActivity();
    }

    private static void onUserActivity() {
        lastActivityTime = System.currentTimeMillis();

        // If user was idle → now ACTIVE → close session
        if (currentlyIdle) {
            endIdleSession();
        }
    }

    // ================= IDLE DETECTION =================

    public static void checkIdle() {

        boolean idleNow = (System.currentTimeMillis() - lastActivityTime) > IDLE_THRESHOLD;

        // Transition: ACTIVE → IDLE
        if (idleNow && !currentlyIdle) {
            startIdleSession();
        }
    }

    private static void startIdleSession() {
        currentlyIdle = true;
        idleStartTime = LocalDateTime.now();

        log.info("🟡 Idle STARTED at {}", idleStartTime);
    }

    private static void endIdleSession() {

        LocalDateTime endTime = LocalDateTime.now();

        long duration = Duration.between(idleStartTime, endTime).getSeconds();

        IdleSessionDto session = IdleSessionDto.builder()
                .startTime(idleStartTime)
                .endTime(endTime)
                .durationSeconds(duration)
                .build();

        synchronized (idleSessions) {
            idleSessions.add(session);
        }

        log.info("🟢 Idle ENDED at {} | Duration: {} sec", endTime, duration);

        currentlyIdle = false;
        idleStartTime = null;
    }

    // ================= GETTERS =================

    public static boolean isIdle() {
        return currentlyIdle;
    }

    public static int getAndResetKeyboard() {
        return keyboardCount.getAndSet(0);
    }

    public static int getAndResetMouse() {
        return mouseCount.getAndSet(0);
    }

    public static List<IdleSessionDto> getAndResetIdleSessions() {

        synchronized (idleSessions) {
            List<IdleSessionDto> copy = new ArrayList<>(idleSessions);
            idleSessions.clear();
            return copy;
        }
    }
}