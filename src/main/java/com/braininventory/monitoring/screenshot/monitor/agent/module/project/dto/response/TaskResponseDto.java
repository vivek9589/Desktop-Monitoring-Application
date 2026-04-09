package com.braininventory.monitoring.screenshot.monitor.agent.module.project.dto.response;


import com.braininventory.monitoring.screenshot.monitor.agent.module.project.enums.TaskStatus;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.enums.Priority;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TaskResponseDto {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private LocalDateTime dueDate;
    private Integer estimatedMinutes;
    private Integer actualMinutes;
    private UUID assignedUserId;
    private UUID projectId;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
