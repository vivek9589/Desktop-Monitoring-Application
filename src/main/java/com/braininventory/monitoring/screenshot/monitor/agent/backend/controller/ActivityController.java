package com.braininventory.monitoring.screenshot.monitor.agent.backend.controller;

import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.ActivityRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.response.ActivityResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.backend.service.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
public class ActivityController {


    private final ActivityService activityService;

    @PostMapping("/activity")
    public ResponseEntity<ActivityResponse> trackActivity(
            @RequestBody ActivityRequest request) {

        log.info("API hit: /activity for agent {}", request.getAgentId());

        ActivityResponse response = activityService.trackActivity(request);

        return ResponseEntity.ok(response);
    }
}
