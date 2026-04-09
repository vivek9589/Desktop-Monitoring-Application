package com.braininventory.monitoring.screenshot.monitor.agent.module.project.service.impl;


import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.OrganizationNotFoundException;
import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.ProjectNotFoundException;
import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.UserNotFoundException;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.entity.Organization;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.repository.OrganizationRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.dto.request.ProjectRequestDto;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.dto.response.ProjectResponseDto;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.entity.Project;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.repository.ProjectRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.module.project.service.ProjectService;
import com.braininventory.monitoring.screenshot.monitor.agent.module.user.entity.User;
import com.braininventory.monitoring.screenshot.monitor.agent.module.user.repository.UserRepository;
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
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    @Override
    public ProjectResponseDto createProject(ProjectRequestDto request) {
        log.info("Creating new project with name: {}", request.getName());

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> {
                    log.error("Organization not found with id: {}", request.getOrganizationId());
                    return new OrganizationNotFoundException(request.getOrganizationId().toString());
                });

        List<User> assignedUsers = resolveAssignedUsers(request.getAssignedUserIds());

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .assignedUsers(assignedUsers)
                .organization(organization)
                .isActive(request.isActive())
                .build();

        Project savedProject = projectRepository.save(project);
        log.info("Project created successfully with id: {}", savedProject.getId());

        return mapToResponse(savedProject);
    }

    @Override
    public ProjectResponseDto updateProject(UUID projectId, ProjectRequestDto request) {
        log.info("Updating project with id: {}", projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.error("Project not found with id: {}", projectId);
                    return new ProjectNotFoundException(projectId.toString());
                });

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        project.setActive(request.isActive());

        if (request.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> {
                        log.error("Organization not found with id: {}", request.getOrganizationId());
                        return new OrganizationNotFoundException(request.getOrganizationId().toString());
                    });
            project.setOrganization(organization);
        }

        if (request.getAssignedUserIds() != null) {
            List<User> assignedUsers = resolveAssignedUsers(request.getAssignedUserIds());
            project.setAssignedUsers(assignedUsers);
        }

        Project updatedProject = projectRepository.save(project);
        log.info("Project updated successfully with id: {}", updatedProject.getId());

        return mapToResponse(updatedProject);
    }

    @Override
    public void deleteProject(UUID projectId) {
        log.info("Deleting project with id: {}", projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.error("Project not found with id: {}", projectId);
                    return new ProjectNotFoundException(projectId.toString());
                });

        projectRepository.delete(project);
        log.info("Project deleted successfully with id: {}", projectId);
    }

    @Override
    public ProjectResponseDto getProjectById(UUID projectId) {
        log.debug("Fetching project with id: {}", projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.error("Project not found with id: {}", projectId);
                    return new ProjectNotFoundException(projectId.toString());
                });

        return mapToResponse(project);
    }

    @Override
    public List<ProjectResponseDto> getAllProjects() {
        log.debug("Fetching all projects");
        return projectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectResponseDto> getProjectsByOrganization(UUID organizationId) {
        log.debug("Fetching projects for organization id: {}", organizationId);

        return projectRepository.findByOrganizationId(organizationId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectResponseDto> getProjectsByUser(UUID userId) {
        log.debug("Fetching projects for user id: {}", userId);

        return projectRepository.findByAssignedUsersId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private List<User> resolveAssignedUsers(List<UUID> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        return userIds.stream()
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> {
                            log.error("User not found with id: {}", id);
                            return new UserNotFoundException(id.toString());
                        }))
                .collect(Collectors.toList());
    }

    private ProjectResponseDto mapToResponse(Project project) {
        return ProjectResponseDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .assignedUserIds(project.getAssignedUsers()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toList()))
                .organizationId(project.getOrganization().getId())
                .isActive(project.isActive())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
