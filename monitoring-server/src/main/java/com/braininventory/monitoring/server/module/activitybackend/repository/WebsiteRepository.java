package com.braininventory.monitoring.server.module.activitybackend.repository;

import com.braininventory.monitoring.server.module.activitybackend.entity.WebsiteUsage;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface WebsiteRepository extends JpaRepository<WebsiteUsage,Long> {


    @Query("""
    SELECT w.domain, SUM(w.durationSeconds)
    FROM WebsiteUsage w
    WHERE w.agentId IN :agentIds
    AND w.startTime BETWEEN :start AND :end
    GROUP BY w.domain
    ORDER BY SUM(w.durationSeconds) DESC
""")
    List<Object[]> getTopWebsitesByAgents(
            @Param("agentIds") List<String> agentIds,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
