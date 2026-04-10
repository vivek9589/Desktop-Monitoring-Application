package com.braininventory.monitoring.server.module.project.service;


import com.braininventory.monitoring.server.module.project.dto.request.TaskRequestDto;
import com.braininventory.monitoring.server.module.project.dto.response.TaskResponseDto;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    TaskResponseDto createTask(TaskRequestDto request);

    TaskResponseDto updateTask(UUID taskId, TaskRequestDto request);

    void deleteTask(UUID taskId);

    TaskResponseDto getTaskById(UUID taskId);

    List<TaskResponseDto> getTasksByProject(UUID projectId);

    List<TaskResponseDto> getTasksByUser(UUID userId);
}

