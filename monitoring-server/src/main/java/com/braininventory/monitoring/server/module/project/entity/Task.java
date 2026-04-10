package com.braininventory.monitoring.server.module.project.entity;


import com.braininventory.monitoring.server.module.project.enums.Priority;
import com.braininventory.monitoring.server.module.project.enums.TaskStatus;
import com.braininventory.monitoring.server.module.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TaskStatus status = TaskStatus.PENDING;

    @Column(name = "priority", length = 50)
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM; // e.g., HIGH, MEDIUM, LOW

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "estimated_minutes")
    private Integer estimatedMinutes;

    @Column(name = "actual_minutes")
    private Integer actualMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id", columnDefinition = "BINARY(16)")
    private User assignedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, columnDefinition = "BINARY(16)")
    private Project project;

//    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comments;

    @Column(name = "comment")
    private String comment;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
