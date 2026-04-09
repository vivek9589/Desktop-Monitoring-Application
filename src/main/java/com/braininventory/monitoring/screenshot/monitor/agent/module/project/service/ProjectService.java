package com.braininventory.monitoring.screenshot.monitor.agent.module.project.service;


import com.braininventory.monitoring.screenshot.monitor.agent.module.project.dto.request.ProjectRequestDto;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.dto.response.ProjectResponseDto;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    ProjectResponseDto createProject(ProjectRequestDto request);

    ProjectResponseDto updateProject(UUID projectId, ProjectRequestDto request);

    void deleteProject(UUID projectId);

    ProjectResponseDto getProjectById(UUID projectId);

    List<ProjectResponseDto> getAllProjects();

    List<ProjectResponseDto> getProjectsByOrganization(UUID organizationId);

    List<ProjectResponseDto> getProjectsByUser(UUID userId);
}

