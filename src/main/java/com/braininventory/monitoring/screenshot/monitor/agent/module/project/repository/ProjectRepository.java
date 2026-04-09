package com.braininventory.monitoring.screenshot.monitor.agent.module.project.repository;


import com.braininventory.monitoring.screenshot.monitor.agent.module.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    // Find all projects belonging to a specific organization
    List<Project> findByOrganizationId(UUID organizationId);

    // Find all projects where a given user is assigned
    List<Project> findByAssignedUsersId(UUID userId);
}

