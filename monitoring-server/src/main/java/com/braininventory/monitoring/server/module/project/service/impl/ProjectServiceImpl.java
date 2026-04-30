package com.braininventory.monitoring.server.module.project.service.impl;

import com.braininventory.monitoring.common.exception.ResourceNotFoundException;
import com.braininventory.monitoring.server.module.organization.entity.Organization;
import com.braininventory.monitoring.server.module.organization.repository.OrganizationRepository;
import com.braininventory.monitoring.server.module.project.dto.request.AddUserToProjectRequest;
import com.braininventory.monitoring.server.module.project.dto.request.ProjectRequest;
import com.braininventory.monitoring.server.module.project.dto.request.ProjectUpdateRequest;
import com.braininventory.monitoring.server.module.project.dto.response.ProjectResponse;
import com.braininventory.monitoring.server.module.project.entity.Project;
import com.braininventory.monitoring.server.module.project.repository.ProjectRepository;
import com.braininventory.monitoring.server.module.project.service.ProjectService;
import com.braininventory.monitoring.server.module.user.entity.User;
import com.braininventory.monitoring.server.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    public ProjectResponse create(ProjectRequest request) {
        log.info("Creating project: {}", request.getName());

        Organization org = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        List<User> users = userRepository.findAllById(request.getAssignedUserIds());

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .organization(org)
                .assignedUsers(users)
                .isActive(true)
                .build();

        Project saved = projectRepository.save(project);
        log.debug("Project created with ID: {}", saved.getId());
        return mapToResponse(saved);
    }

    public ProjectResponse get(UUID id) {
        log.debug("Fetching project with id: {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        return mapToResponse(project);
    }

    public List<ProjectResponse> getAll() {
        log.debug("Fetching all projects");
        return projectRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProjectResponse update(UUID id, ProjectUpdateRequest request) {
        log.info("Updating project with id: {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (request.getName() != null) project.setName(request.getName());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getStartDate() != null) project.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) project.setEndDate(request.getEndDate());
        if (request.getIsActive() != null) project.setActive(request.getIsActive());

        if (request.getAssignedUserIds() != null) {
            List<User> users = userRepository.findAllById(request.getAssignedUserIds());
            project.setAssignedUsers(users);
        }

        Project updated = projectRepository.save(project);
        log.debug("Project updated with ID: {}", updated.getId());
        return mapToResponse(updated);
    }

    public void delete(UUID id) {
        log.warn("Deleting project with id: {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        projectRepository.delete(project);
    }

    @Override
    public ProjectResponse addUsersToProject(AddUserToProjectRequest request) {
        log.info("Adding users {} to project {}", request.getUserIds(), request.getProjectId());

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        List<User> users = userRepository.findAllById(request.getUserIds());
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No valid users found for given IDs");
        }

        project.getAssignedUsers().addAll(users);
        Project saved = projectRepository.save(project);

        log.debug("Users added to project with ID: {}", saved.getId());

        return ProjectResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .startDate(saved.getStartDate())
                .endDate(saved.getEndDate())
                .organizationId(saved.getOrganization().getId())
                .assignedUserIds(saved.getAssignedUsers().stream()
                        .map(User::getId)
                        .toList())
                .build();
    }

    private ProjectResponse mapToResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .organizationId(project.getOrganization().getId())
                .assignedUserIds(project.getAssignedUsers().stream()
                        .map(User::getId)
                        .toList())
                .isActive(project.isActive())
                .createdAt(project.getCreatedAt())
                .build();
    }
}
