package com.braininventory.monitoring.server.module.activitybackend.service;


import com.braininventory.monitoring.server.module.activitybackend.entity.AppUsageSession;

public interface AppUsageService {
    void save(AppUsageSession session);
}
