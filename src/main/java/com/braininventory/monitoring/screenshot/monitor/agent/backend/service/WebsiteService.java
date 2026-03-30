package com.braininventory.monitoring.screenshot.monitor.agent.backend.service;

import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.WebsiteUsageDto;

public interface WebsiteService {

    void save(WebsiteUsageDto dto);
}
