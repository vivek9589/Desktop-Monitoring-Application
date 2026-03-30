package com.braininventory.monitoring.screenshot.monitor.agent.backend.service;

import com.braininventory.monitoring.screenshot.monitor.agent.backend.entity.AppUsageSession;


public interface AppUsageService {
    void save(AppUsageSession session);
}
