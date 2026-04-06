package com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "idle_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdleSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String agentId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long durationSeconds;
}
