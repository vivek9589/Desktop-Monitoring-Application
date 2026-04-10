package com.braininventory.monitoring.server.module.activitybackend.service;


import com.braininventory.monitoring.common.dto.request.WebsiteUsageDto;

public interface WebsiteService {

    void save(WebsiteUsageDto dto);
}
