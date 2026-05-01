package com.braininventory.monitoring.server.module.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebsiteUsageDTO {
    private String domain;
    private long duration;
}