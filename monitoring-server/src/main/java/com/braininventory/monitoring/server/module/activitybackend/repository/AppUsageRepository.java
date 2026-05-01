package com.braininventory.monitoring.server.module.activitybackend.repository;

import com.braininventory.monitoring.server.module.activitybackend.entity.AppUsageSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppUsageRepository extends JpaRepository<AppUsageSession,Long> {


    @Query("""
    SELECT a.appName, SUM(a.durationSeconds)
    FROM AppUsageSession a
    WHERE a.agentId IN :agentIds
    AND a.startTime BETWEEN :start AND :end
    GROUP BY a.appName
    ORDER BY SUM(a.durationSeconds) DESC
""")
    List<Object[]> getTopAppsByAgents(
            @Param("agentIds") List<String> agentIds,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
