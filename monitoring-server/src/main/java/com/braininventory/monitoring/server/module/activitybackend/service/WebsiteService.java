package com.braininventory.monitoring.server.module.activitybackend.service;


import com.braininventory.monitoring.common.dto.request.WebsiteUsageDto;
import com.braininventory.monitoring.server.module.dashboard.dto.response.WebsiteUsageDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface WebsiteService {

    void save(WebsiteUsageDto dto);

    List<WebsiteUsageDTO> getTopWebsitesByAgents(List<String> agentIds, LocalDate start, LocalDate end);
}
