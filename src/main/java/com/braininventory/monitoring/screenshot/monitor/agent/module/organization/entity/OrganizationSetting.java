package com.braininventory.monitoring.screenshot.monitor.agent.module.organization.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents organization-specific settings such as working hours, days, and timezone.
 * Uses shared primary key with Organization entity for strong one-to-one mapping.
 */
@Entity
@Table(name = "organization_setting")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationSetting {

    @Id
    @Column(name = "organization_id", updatable = false, nullable = false)
    private UUID organizationId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "working_hours_per_day", nullable = false)
    private Integer workingHoursPerDay;

    @Column(name = "working_days_per_week", nullable = false)
    private Integer workingDaysPerWeek;

    @Column(name = "timezone", length = 100)
    private String timezone;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}