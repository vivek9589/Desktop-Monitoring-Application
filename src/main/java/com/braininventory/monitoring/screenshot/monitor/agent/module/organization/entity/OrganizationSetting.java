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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;   // unique PK for settings

    @OneToOne
    @JoinColumn(name = "organization_id", nullable = false, unique = true)
    private Organization organization;

    @Column(name = "working_hours_per_day", nullable = false)
    private Integer workingHoursPerDay;

    @Column(name = "working_days_per_week", nullable = false)
    private Integer workingDaysPerWeek;

    @Column(name = "timezone", length = 100)
    private String timezone;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    private Long version; // optimistic locking
}
