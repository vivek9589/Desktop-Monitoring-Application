package com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.service;

import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.WebsiteUsageDto;

public interface WebsiteService {

    void save(WebsiteUsageDto dto);
}
