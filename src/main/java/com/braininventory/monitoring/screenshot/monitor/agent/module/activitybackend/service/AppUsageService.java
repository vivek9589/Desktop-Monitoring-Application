package com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.service;

import com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.entity.AppUsageSession;


public interface AppUsageService {
    void save(AppUsageSession session);
}
