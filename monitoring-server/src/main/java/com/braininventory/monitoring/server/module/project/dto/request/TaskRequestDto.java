package com.braininventory.monitoring.server.module.project.dto.request;

import com.braininventory.monitoring.server.module.project.enums.TaskStatus;
import com.braininventory.monitoring.server.module.project.enums.Priority;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TaskRequestDto {
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private LocalDateTime dueDate;
    private Integer estimatedMinutes;
    private UUID assignedUserId;
    private UUID projectId;
    private String comment;
}