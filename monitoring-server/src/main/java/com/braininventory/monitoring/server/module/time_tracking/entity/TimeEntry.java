package com.braininventory.monitoring.server.module.time_tracking.entity;


import com.braininventory.monitoring.server.module.project.entity.Task;
import com.braininventory.monitoring.server.module.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "time_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false, columnDefinition = "BINARY(16)")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    private User user;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer minutesLogged;

    private Integer computerActivityMinutes; // new field

    private Integer productiveMinutes; // new field

    private String notes;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}
