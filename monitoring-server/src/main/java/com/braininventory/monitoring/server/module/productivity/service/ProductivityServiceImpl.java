package com.braininventory.monitoring.server.module.productivity.service;

import com.braininventory.monitoring.common.dto.response.ProductivityReportDto;
import com.braininventory.monitoring.server.module.productivity.repository.ProductivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductivityServiceImpl implements ProductivityService {

    private final ProductivityRepository repository;

    private ProductivityReportDto mapToDto(String agentId, Object result) {
        // 1. Handle Null
        if (result == null) {
            return new ProductivityReportDto(agentId, 0L, 0L, 0L, 0.0);
        }

        Object[] row;
        // 2. Handle potential nested array from some JPA/Hibernate versions
        if (result instanceof Object[]) {
            Object[] temp = (Object[]) result;
            if (temp.length > 0 && temp[0] instanceof Object[]) {
                row = (Object[]) temp[0];
            } else {
                row = temp;
            }
        } else {
            return new ProductivityReportDto(agentId, 0L, 0L, 0L, 0.0);
        }

        // 3. Safe Extraction
        long idleTime = row[0] != null ? ((Number) row[0]).longValue() : 0L;
        long productiveAppTime = row[1] != null ? ((Number) row[1]).longValue() : 0L;
        long productiveWebTime = row[2] != null ? ((Number) row[2]).longValue() : 0L;

        ProductivityReportDto dto = new ProductivityReportDto(agentId, idleTime, productiveAppTime, productiveWebTime, 0.0);
        dto.calculateScore();
        return dto;
    }

    @Override
    public ProductivityReportDto getTodayReport(String agentId) {
        LocalDate today = LocalDate.now();
        Object[] result = repository.getReportForRange(agentId, today.atStartOfDay(), today.atTime(LocalTime.MAX));
        return mapToDto(agentId, result);
    }

    @Override
    public ProductivityReportDto getYesterdayReport(String agentId) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Object[] result = repository.getReportForRange(agentId, yesterday.atStartOfDay(), yesterday.atTime(LocalTime.MAX));
        return mapToDto(agentId, result);
    }

    @Override
    public ProductivityReportDto getWeeklyReport(String agentId) {
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY);
        Object[] result = repository.getReportForRange(agentId, startOfWeek.atStartOfDay(), endOfWeek.atTime(LocalTime.MAX));
        return mapToDto(agentId, result);
    }

    @Override
    public ProductivityReportDto getCustomReport(String agentId, LocalDate start, LocalDate end) {
        Object[] result = repository.getReportForRange(agentId, start.atStartOfDay(), end.atTime(LocalTime.MAX));
        return mapToDto(agentId, result);
    }

    @Override
    public List<ProductivityReportDto> getTrendReport(String agentId, LocalDate start, LocalDate end) {
        List<ProductivityReportDto> reports = new ArrayList<>();
        LocalDate current = start;

        while (!current.isAfter(end)) {
            Object[] result = repository.getReportForRange(agentId, current.atStartOfDay(), current.atTime(LocalTime.MAX));
            reports.add(mapToDto(agentId, result));
            current = current.plusDays(1);
        }

        return reports;
    }
}