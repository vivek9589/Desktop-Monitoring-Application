package com.braininventory.monitoring.server.module.organization.entity;



import com.braininventory.monitoring.server.module.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Represents an Organization entity with settings and monitoring configurations.
 * Ensures robust mapping with explicit column names and constraints.
 */

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 255)
    private String addressLine1;

    @Column(length = 255)
    private String addressLine2;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 20)
    private String postalCode;

    @Column(length = 100)
    private String country;

    @Column(length = 50)
    private String phoneNumber;

    @Column(length = 150)
    private String contactEmail;

    @Column(name = "active", nullable = false)
    private boolean isActive = true;  // renamed for clarity

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

    @OneToOne(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private OrganizationSetting organizationSetting;

    @OneToOne(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private MonitoringSetting monitoringSetting;

    @Column(name = "timezone", length = 100)
    private String timezone = "Asia/Kolkata";

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // helper methods
    public void addProject(Project project) {
        projects.add(project);
        project.setOrganization(this);
    }

    public void removeProject(Project project) {
        projects.remove(project);
        project.setOrganization(null);
    }
}
