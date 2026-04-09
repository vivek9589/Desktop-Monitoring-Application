package com.braininventory.monitoring.screenshot.monitor.agent.module.project.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.module.project.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByProjectId(UUID projectId);

    List<Task> findByAssignedUserId(UUID userId);


}