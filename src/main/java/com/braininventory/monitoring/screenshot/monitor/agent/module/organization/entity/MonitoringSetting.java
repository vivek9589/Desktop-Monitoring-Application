package com.braininventory.monitoring.screenshot.monitor.agent.module.organization.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents monitoring-specific settings for an organization,
 * including screenshot intervals, storage paths, API endpoints,
 * web tracking, and input tracking configurations.
 * Uses shared primary key with Organization entity for strong one-to-one mapping.
 */
@Entity
@Table(name = "monitoring_setting")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoringSetting {

    @Id
    @Column(name = "organization_id", updatable = false, nullable = false)
    private UUID organizationId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    // ==========================
    // Agent Behavior (Intervals & Paths)
    // ==========================
    @Column(name = "screenshot_frequency_minutes", nullable = false)
    @ColumnDefault("20")
    private Integer screenshotFrequencyMinutes = 20;

    @Column(name = "screenshot_storage_path", length = 500)
    @ColumnDefault("'./screenshots'")
    private String screenshotStoragePath = "./screenshots";

    @Column(name = "screenshot_upload_path", length = 500)
    @ColumnDefault("'/api/screenshots/upload'")
    private String screenshotUploadPath = "/api/screenshots/upload";

    @Column(name = "os_type", length = 50)
    @ColumnDefault("'windows'")
    private String osType = "windows";

    @Column(name = "agent_app_interval_ms", nullable = false)
    @ColumnDefault("5000")
    private Integer agentAppIntervalMs = 5000;

    // ============================
    // Web Tracking Agent
    // ============================
    @Column(name = "website_api_path", length = 500)
    @ColumnDefault("'/api/website-usage'")
    private String websiteApiPath = "/api/website-usage";

    @Column(name = "website_interval_ms", nullable = false)
    @ColumnDefault("5000")
    private Integer websiteIntervalMs = 5000;

    // ============================
    // Mouse/Keyboard Tracking Agent
    // ============================
    @Column(name = "activity_rate_ms", nullable = false)
    @ColumnDefault("5000")
    private Integer activityRateMs = 5000;

    @Column(name = "idle_check_rate_ms", nullable = false)
    @ColumnDefault("5000")
    private Integer idleCheckRateMs = 5000;

    // ============================
    // General Monitoring Flags
    // ============================
    @Column(name = "blur_screenshots", nullable = false)
    @ColumnDefault("false")
    private Boolean blurScreenshots = false;

    // ============================
    // Audit Fields
    // ============================
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}