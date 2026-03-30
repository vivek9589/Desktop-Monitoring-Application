package com.braininventory.monitoring.screenshot.monitor.agent.backend.entity;

import com.braininventory.monitoring.screenshot.monitor.agent.backend.enums.WebsiteCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entity representing website usage sessions.
 */
@Entity
@Table(name = "website_usage")
@Data
public class WebsiteUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String url;

    private String title;

    @Column(nullable=false)
    private String domain;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long duration;

    @Enumerated(EnumType.STRING)
    private WebsiteCategory category;
}