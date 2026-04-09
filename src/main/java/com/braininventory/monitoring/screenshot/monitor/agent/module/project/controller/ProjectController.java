package com.braininventory.monitoring.screenshot.monitor.agent.module.project.controller;


import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.ApiResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.dto.request.ProjectRequestDto;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.dto.response.ProjectResponseDto;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponseDto>> createProject(@RequestBody ProjectRequestDto request) {
        log.info("Received request to create project: {}", request.getName());
        ProjectResponseDto responseDto = projectService.createProject(request);
        return ResponseEntity.ok(ApiResponse.success(responseDto, UUID.randomUUID().toString()));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> updateProject(
            @PathVariable UUID projectId,
            @RequestBody ProjectRequestDto request) {
        log.info("Received request to update project with id: {}", projectId);
        ProjectResponseDto responseDto = projectService.updateProject(projectId, request);
        return ResponseEntity.ok(ApiResponse.success(responseDto, UUID.randomUUID().toString()));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable UUID projectId) {
        log.info("Received request to delete project with id: {}", projectId);
        projectService.deleteProject(projectId);
        return ResponseEntity.ok(ApiResponse.success(null, UUID.randomUUID().toString()));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> getProjectById(@PathVariable UUID projectId) {
        log.debug("Received request to fetch project with id: {}", projectId);
        ProjectResponseDto responseDto = projectService.getProjectById(projectId);
        return ResponseEntity.ok(ApiResponse.success(responseDto, UUID.randomUUID().toString()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getAllProjects() {
        log.debug("Received request to fetch all projects");
        List<ProjectResponseDto> projects = projectService.getAllProjects();
        return ResponseEntity.ok(ApiResponse.success(projects, UUID.randomUUID().toString()));
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getProjectsByOrganization(@PathVariable UUID organizationId) {
        log.debug("Received request to fetch projects for organization id: {}", organizationId);
        List<ProjectResponseDto> projects = projectService.getProjectsByOrganization(organizationId);
        return ResponseEntity.ok(ApiResponse.success(projects, UUID.randomUUID().toString()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getProjectsByUser(@PathVariable UUID userId) {
        log.debug("Received request to fetch projects for user id: {}", userId);
        List<ProjectResponseDto> projects = projectService.getProjectsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(projects, UUID.randomUUID().toString()));
    }
}

