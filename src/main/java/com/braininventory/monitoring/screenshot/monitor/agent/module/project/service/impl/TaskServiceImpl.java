package com.braininventory.monitoring.screenshot.monitor.agent.module.project.service.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.ProjectNotFoundException;
import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.TaskNotFoundException;
import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.UserNotFoundException;

import com.braininventory.monitoring.screenshot.monitor.agent.module.project.dto.request.TaskRequestDto;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.dto.response.TaskResponseDto;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.entity.Project;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.entity.Task;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.repository.TaskRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.service.TaskService;
import com.braininventory.monitoring.screenshot.monitor.agent.module.user.entity.User;
import com.braininventory.monitoring.screenshot.monitor.agent.module.user.repository.UserRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    public TaskResponseDto createTask(TaskRequestDto request) {
        log.info("Creating new task with title: {}", request.getTitle());

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> {
                    log.error("Project not found with id: {}", request.getProjectId());
                    return new ProjectNotFoundException(request.getProjectId().toString());
                });

        User assignedUser = null;
        if (request.getAssignedUserId() != null) {
            assignedUser = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> {
                        log.error("User not found with id: {}", request.getAssignedUserId());
                        return new UserNotFoundException(request.getAssignedUserId().toString());
                    });
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .estimatedMinutes(request.getEstimatedMinutes())
                .assignedUser(assignedUser)
                .project(project)
                .comment(request.getComment())
                .build();

        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with id: {}", savedTask.getId());

        return mapToResponse(savedTask);
    }

    @Override
    public TaskResponseDto updateTask(UUID taskId, TaskRequestDto request) {
        log.info("Updating task with id: {}", taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("Task not found with id: {}", taskId);
                    return new TaskNotFoundException(taskId.toString());
                });

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setEstimatedMinutes(request.getEstimatedMinutes());
        task.setComment(request.getComment());

        if (request.getAssignedUserId() != null) {
            User assignedUser = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> {
                        log.error("User not found with id: {}", request.getAssignedUserId());
                        return new UserNotFoundException(request.getAssignedUserId().toString());
                    });
            task.setAssignedUser(assignedUser);
        }

        Task updatedTask = taskRepository.save(task);
        log.info("Task updated successfully with id: {}", updatedTask.getId());

        return mapToResponse(updatedTask);
    }

    @Override
    public void deleteTask(UUID taskId) {
        log.info("Deleting task with id: {}", taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("Task not found with id: {}", taskId);
                    return new TaskNotFoundException(taskId.toString());
                });

        taskRepository.delete(task);
        log.info("Task deleted successfully with id: {}", taskId);
    }

    @Override
    public TaskResponseDto getTaskById(UUID taskId) {
        log.debug("Fetching task with id: {}", taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("Task not found with id: {}", taskId);
                    return new TaskNotFoundException(taskId.toString());
                });

        return mapToResponse(task);
    }

    @Override
    public List<TaskResponseDto> getTasksByProject(UUID projectId) {
        log.debug("Fetching tasks for project id: {}", projectId);

        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByUser(UUID userId) {
        log.debug("Fetching tasks for user id: {}", userId);

        return taskRepository.findByAssignedUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TaskResponseDto mapToResponse(Task task) {
        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .estimatedMinutes(task.getEstimatedMinutes())
                .actualMinutes(task.getActualMinutes())
                .assignedUserId(task.getAssignedUser() != null ? task.getAssignedUser().getId() : null)
                .projectId(task.getProject().getId())
                .comment(task.getComment())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
