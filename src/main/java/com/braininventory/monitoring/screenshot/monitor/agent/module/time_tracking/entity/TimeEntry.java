package com.braininventory.monitoring.screenshot.monitor.agent.module.time_tracking.entity;

import com.braininventory.monitoring.screenshot.monitor.agent.module.project.entity.Task;
import com.braininventory.monitoring.screenshot.monitor.agent.module.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "time_entries")
public class TimeEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer minutesLogged;
    private String notes;
}
