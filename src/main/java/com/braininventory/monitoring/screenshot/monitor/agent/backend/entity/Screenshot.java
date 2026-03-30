package com.braininventory.monitoring.screenshot.monitor.agent.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "screenshots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Screenshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_id", nullable = false)
    private String agentId;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "timestamp", nullable = false)
    private String timestamp;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}