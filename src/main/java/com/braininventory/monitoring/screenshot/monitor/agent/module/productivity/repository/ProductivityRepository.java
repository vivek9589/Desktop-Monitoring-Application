package com.braininventory.monitoring.screenshot.monitor.agent.module.productivity.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.entity.AppUsageSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface ProductivityRepository extends JpaRepository<AppUsageSession, Long> {

    @Query(value = """
    SELECT 
      (SELECT COALESCE(SUM(duration_seconds), 0) FROM idle_sessions 
       WHERE agent_id = :agentId AND start_time BETWEEN :startDate AND :endDate) AS idleTime,
      
      (SELECT COALESCE(SUM(duration_seconds), 0) FROM app_usage_sessions 
       WHERE agent_id = :agentId AND category = 'PRODUCTIVE' AND start_time BETWEEN :startDate AND :endDate) AS productiveAppTime,
      
      (SELECT COALESCE(SUM(duration_seconds), 0) FROM website_usage 
       WHERE agent_id = :agentId AND category = 'PRODUCTIVE' AND start_time BETWEEN :startDate AND :endDate) AS productiveWebTime
    """, nativeQuery = true)
    Object[] getReportForRange(@Param("agentId") String agentId,
                               @Param("startDate") LocalDateTime startDate,
                               @Param("endDate") LocalDateTime endDate);
}