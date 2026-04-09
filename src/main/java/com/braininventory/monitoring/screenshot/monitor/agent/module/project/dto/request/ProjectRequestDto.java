package com.braininventory.monitoring.screenshot.monitor.agent.module.project.dto.request;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ProjectRequestDto {
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<UUID> assignedUserIds;   // IDs of users to assign
    private UUID organizationId;          // Required
    private boolean isActive = true;
}

