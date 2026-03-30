package com.braininventory.monitoring.screenshot.monitor.agent.backend.entity;

import com.braininventory.monitoring.screenshot.monitor.agent.backend.enums.WebsiteCategory;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity representing classification rules for domains.
 */
@Entity
@Table(name = "website_classification")
@Data
public class WebsiteClassification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable=false)
    private String domain;

    @Enumerated(EnumType.STRING)
    private WebsiteCategory category;
}