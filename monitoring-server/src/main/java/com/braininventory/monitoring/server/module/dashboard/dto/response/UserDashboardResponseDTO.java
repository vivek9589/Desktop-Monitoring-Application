package com.braininventory.monitoring.server.module.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDashboardResponseDTO {

    private UserProductivityDTO productivity;
    private List<TrendDTO> trend;
    private List<AppUsageDTO> topApps;
    private List<WebsiteUsageDTO> topWebsites;
    private List<ScreenshotDTO> screenshots;
}