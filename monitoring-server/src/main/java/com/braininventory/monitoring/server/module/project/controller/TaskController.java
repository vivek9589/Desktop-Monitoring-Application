package com.braininventory.monitoring.server.module.project.controller;
import com.braininventory.monitoring.common.dto.ApiResponse;
import com.braininventory.monitoring.server.module.project.dto.request.TaskRequestDto;
import com.braininventory.monitoring.server.module.project.dto.response.TaskResponseDto;
import com.braininventory.monitoring.server.module.project.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponseDto>> createTask(@RequestBody TaskRequestDto request) {
        log.info("Received request to create task: {}", request.getTitle());
        TaskResponseDto responseDto = taskService.createTask(request);
        return ResponseEntity.ok(ApiResponse.success(responseDto, UUID.randomUUID().toString()));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponseDto>> updateTask(
            @PathVariable UUID taskId,
            @RequestBody TaskRequestDto request) {
        log.info("Received request to update task with id: {}", taskId);
        TaskResponseDto responseDto = taskService.updateTask(taskId, request);
        return ResponseEntity.ok(ApiResponse.success(responseDto, UUID.randomUUID().toString()));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable UUID taskId) {
        log.info("Received request to delete task with id: {}", taskId);
        taskService.deleteTask(taskId);
        return ResponseEntity.ok(ApiResponse.success(null, UUID.randomUUID().toString()));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponseDto>> getTaskById(@PathVariable UUID taskId) {
        log.debug("Received request to fetch task with id: {}", taskId);
        TaskResponseDto responseDto = taskService.getTaskById(taskId);
        return ResponseEntity.ok(ApiResponse.success(responseDto, UUID.randomUUID().toString()));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getTasksByProject(@PathVariable UUID projectId) {
        log.debug("Received request to fetch tasks for project id: {}", projectId);
        List<TaskResponseDto> tasks = taskService.getTasksByProject(projectId);
        return ResponseEntity.ok(ApiResponse.success(tasks, UUID.randomUUID().toString()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getTasksByUser(@PathVariable UUID userId) {
        log.debug("Received request to fetch tasks for user id: {}", userId);
        List<TaskResponseDto> tasks = taskService.getTasksByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(tasks, UUID.randomUUID().toString()));
    }
}
