package com.braininventory.monitoring.screenshot.monitor.agent.backend.entity;



import com.braininventory.monitoring.screenshot.monitor.agent.backend.enums.ActivityStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs",
        indexes = {
                @Index(name = "idx_agent_time", columnList = "agent_id, timestamp")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_id", nullable = false)
    private String agentId;

    @Column(nullable = false)
    private int keyboardCount;

    @Column(nullable = false)
    private int mouseCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityStatus status; // ACTIVE / IDLE

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}