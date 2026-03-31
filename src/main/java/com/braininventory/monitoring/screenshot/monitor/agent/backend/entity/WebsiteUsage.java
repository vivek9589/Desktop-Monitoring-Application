package com.braininventory.monitoring.screenshot.monitor.agent.backend.entity;

import com.braininventory.monitoring.screenshot.monitor.agent.backend.enums.Category;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity representing website usage sessions.
 */
@Entity
@Table(
        name = "website_usage",
        indexes = {
                @Index(name = "idx_agent_id", columnList = "agent_id"),
                @Index(name = "idx_domain", columnList = "domain")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteUsage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_id", nullable = false, length = 100)
    private String agentId;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "domain", nullable = false, length = 255)
    private String domain;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration_seconds")
    private long durationSeconds;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @PrePersist
    @PreUpdate
    private void calculateDuration() {
        if (startTime != null && endTime != null) {
            this.durationSeconds = java.time.Duration.between(startTime, endTime).getSeconds();
        }
    }
}