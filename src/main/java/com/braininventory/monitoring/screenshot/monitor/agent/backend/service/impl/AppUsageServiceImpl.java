package com.braininventory.monitoring.screenshot.monitor.agent.backend.service.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.backend.entity.AppUsageSession;
import com.braininventory.monitoring.screenshot.monitor.agent.backend.repository.AppUsageRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.backend.service.AppUsageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AppUsageServiceImpl implements AppUsageService {

    private final AppUsageRepository repository;

    @Override
    public void save(AppUsageSession session) {
        try {
            repository.save(session);
            log.info("Saved AppUsageSession for agentId={} and appName={}",
                    session.getAgentId(), session.getAppName());
        } catch (Exception e) {
            log.error("Failed to save AppUsageSession for agentId={}: {}",
                    session.getAgentId(), e.getMessage(), e);
            throw new RuntimeException("Error saving app usage session", e);
        }
    }
}
