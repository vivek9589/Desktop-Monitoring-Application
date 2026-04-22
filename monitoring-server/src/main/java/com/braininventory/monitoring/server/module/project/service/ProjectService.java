package com.braininventory.monitoring.server.module.project.service;




import com.braininventory.monitoring.server.module.project.dto.request.AddUserToProjectRequest;
import com.braininventory.monitoring.server.module.project.dto.request.ProjectRequest;
import com.braininventory.monitoring.server.module.project.dto.request.ProjectUpdateRequest;
import com.braininventory.monitoring.server.module.project.dto.response.ProjectResponse;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    ProjectResponse create(ProjectRequest request);

    ProjectResponse get(UUID id);

    List<ProjectResponse> getAll();

    ProjectResponse update(UUID id, ProjectUpdateRequest request);

    void delete(UUID id);


    ProjectResponse addUsersToProject(AddUserToProjectRequest request);
}

