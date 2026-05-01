package com.braininventory.monitoring.server.module.activitybackend.service;


import com.braininventory.monitoring.server.module.activitybackend.entity.AppUsageSession;
import com.braininventory.monitoring.server.module.dashboard.dto.response.AppUsageDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AppUsageService {
    void save(AppUsageSession session);


    public List<AppUsageDTO> getTopAppsByAgents(List<String> agentIds, LocalDate start, LocalDate end);
}
