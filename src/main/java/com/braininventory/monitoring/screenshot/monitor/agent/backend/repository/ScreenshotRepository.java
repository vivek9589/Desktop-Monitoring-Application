package com.braininventory.monitoring.screenshot.monitor.agent.backend.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.backend.entity.Screenshot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;

public interface ScreenshotRepository extends JpaRepository<Screenshot, Long> {
    Page<Screenshot> findByAgentIdAndTimestampBetween(
            String agentId, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
