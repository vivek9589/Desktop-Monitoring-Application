package com.braininventory.monitoring.server.module.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SummaryDTO {
    private int totalEmployees;
    private int activeUsers;
    private int totalProjects;
}
