package com.braininventory.monitoring.screenshot.monitor.agent.monitoring.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "app_usage_sessions",
        indexes = {
                @Index(name = "idx_agent_id", columnList = "agent_id"),
                @Index(name = "idx_app_name", columnList = "app_name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUsageSession implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_id", nullable = false, length = 100)
    private String agentId;

    @Column(name = "app_name", nullable = false, length = 200)
    private String appName;

    @Column(name = "window_title", length = 500)
    private String windowTitle;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration_seconds")
    private long durationSeconds;

    @PrePersist
    @PreUpdate
    private void calculateDuration() {
        if (startTime != null && endTime != null) {
            this.durationSeconds = java.time.Duration.between(startTime, endTime).getSeconds();
        }
    }
}