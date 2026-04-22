package com.braininventory.monitoring.server.module.project.controller;

import com.braininventory.monitoring.common.dto.ApiResponse;
import com.braininventory.monitoring.server.module.project.dto.request.AddUserToProjectRequest;
import com.braininventory.monitoring.server.module.project.dto.request.ProjectRequest;
import com.braininventory.monitoring.server.module.project.dto.request.ProjectUpdateRequest;
import com.braininventory.monitoring.server.module.project.dto.response.ProjectResponse;
import com.braininventory.monitoring.server.module.project.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> create(
            @RequestBody ProjectRequest request,
            HttpServletRequest httpRequest) {
        ProjectResponse response = projectService.create(request);
        return ResponseEntity.ok(ApiResponse.success(response, httpRequest.getRequestId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> get(
            @PathVariable UUID id,
            HttpServletRequest httpRequest) {
        ProjectResponse response = projectService.get(id);
        return ResponseEntity.ok(ApiResponse.success(response, httpRequest.getRequestId()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAll(HttpServletRequest httpRequest) {
        List<ProjectResponse> response = projectService.getAll();
        return ResponseEntity.ok(ApiResponse.success(response, httpRequest.getRequestId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> update(
            @PathVariable UUID id,
            @RequestBody ProjectUpdateRequest request,
            HttpServletRequest httpRequest) {
        ProjectResponse response = projectService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, httpRequest.getRequestId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            HttpServletRequest httpRequest) {
        projectService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, httpRequest.getRequestId()));
    }


    @PostMapping("/{projectId}/users")
    public ResponseEntity<ApiResponse<ProjectResponse>> addUsersToProject(
            @PathVariable UUID projectId,
            @RequestBody AddUserToProjectRequest request,
            HttpServletRequest httpRequest) {

        request.setProjectId(projectId);
        ProjectResponse response = projectService.addUsersToProject(request);

        return ResponseEntity.ok(ApiResponse.success(response, httpRequest.getRequestId()));
    }
}
