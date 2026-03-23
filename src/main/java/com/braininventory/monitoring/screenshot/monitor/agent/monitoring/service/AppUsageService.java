package com.braininventory.monitoring.screenshot.monitor.agent.monitoring.service;

import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.entity.AppUsageSession;


public interface AppUsageService {
    void save(AppUsageSession session);
}
