package com.braininventory.monitoring.screenshot.monitor.agent.monitoring.service.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.ActivityRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.response.ActivityResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.entity.ActivityLog;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.entity.IdleSession;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.enums.ActivityStatus;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.repository.ActivityRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.repository.IdleSessionRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.service.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final IdleSessionRepository idleSessionRepository;

    private static final int IDLE_THRESHOLD = 0; // basic phase

    @Override
    public ActivityResponse trackActivity(ActivityRequest request) {

        log.info("Received activity for agent: {}", request.getAgentId());

        // 1. Save activity log (ONLY data)
        ActivityLog activityLog = ActivityLog.builder()
                .agentId(request.getAgentId())
                .keyboardCount(request.getKeyboardCount())
                .mouseCount(request.getMouseCount())
                .status(request.isIdle() ? ActivityStatus.IDLE : ActivityStatus.ACTIVE)
                .timestamp(request.getTimestamp())
                .createdAt(LocalDateTime.now())
                .build();

        activityRepository.save(activityLog);

        // 2. Save idle sessions (DIRECT)
        if (request.getIdleSessions() != null && !request.getIdleSessions().isEmpty()) {

            request.getIdleSessions().forEach(session -> {

                IdleSession entity = IdleSession.builder()
                        .agentId(request.getAgentId())
                        .startTime(session.getStartTime())
                        .endTime(session.getEndTime())
                        .durationSeconds(session.getDurationSeconds())
                        .build();

                idleSessionRepository.save(entity);
            });

            log.info("Saved {} idle sessions", request.getIdleSessions().size());
        }

        return ActivityResponse.builder()
                .agentId(request.getAgentId())
                .status(request.isIdle() ? "IDLE" : "ACTIVE")
                .timestamp(request.getTimestamp())
                .build();
    }


//    private ActivityStatus determineStatus(ActivityRequest request) {
//        if (request.getKeyboardCount() == 0 && request.getMouseCount() == 0) {
//            return ActivityStatus.IDLE;
//        }
//        return ActivityStatus.ACTIVE;
//    }
//
//    private void handleIdleSession(String agentId, ActivityStatus status, LocalDateTime timestamp) {
//
//        Optional<IdleSession> lastSessionOpt =
//                idleSessionRepository.findTopByAgentIdOrderByStartTimeDesc(agentId);
//
//        if (status == ActivityStatus.IDLE) {
//
//            if (lastSessionOpt.isEmpty() || lastSessionOpt.get().getEndTime() != null) {
//                // Start new idle session
//                IdleSession session = IdleSession.builder()
//                        .agentId(agentId)
//                        .startTime(timestamp)
//                        .build();
//
//                idleSessionRepository.save(session);
//                log.info("Started new idle session for agent {}", agentId);
//            }
//
//        } else {
//
//            if (lastSessionOpt.isPresent() && lastSessionOpt.get().getEndTime() == null) {
//
//                IdleSession session = lastSessionOpt.get();
//                session.setEndTime(timestamp);
//
//                long duration = java.time.Duration.between(
//                        session.getStartTime(), timestamp).getSeconds();
//
//                session.setDurationSeconds(duration);
//
//                idleSessionRepository.save(session);
//
//                log.info("Closed idle session for agent {}, duration: {} sec", agentId, duration);
//            }
//        }
//    }
}
