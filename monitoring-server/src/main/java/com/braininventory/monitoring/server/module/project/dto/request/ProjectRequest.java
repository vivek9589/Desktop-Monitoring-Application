package com.braininventory.monitoring.server.module.project.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// Request DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID organizationId;
    private List<UUID> assignedUserIds;
}

