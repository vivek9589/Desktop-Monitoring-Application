package com.braininventory.monitoring.screenshot.monitor.agent.module.project.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.module.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
}
