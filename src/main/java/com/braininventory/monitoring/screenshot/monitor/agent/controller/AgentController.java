package com.braininventory.monitoring.screenshot.monitor.agent.controller;

import com.braininventory.monitoring.screenshot.monitor.agent.dto.ApiResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.dto.request.AgentHeartbeatRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.dto.response.AgentResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.service.AgentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
@Slf4j
public class AgentController {

    private final AgentService agentService;

    @PostMapping("/heartbeat")
    public ResponseEntity<ApiResponse<AgentResponse>> heartbeat(
            @RequestBody AgentHeartbeatRequest request,
            HttpServletRequest httpRequest) {

        String requestId = UUID.randomUUID().toString();
        try {
            String ipAddress = httpRequest.getRemoteAddr();
            AgentResponse response = agentService.registerOrUpdateAgent(request, ipAddress);

            return ResponseEntity.ok(ApiResponse.success(response, requestId));
        } catch (Exception e) {
            log.error("Error processing heartbeat", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("HEARTBEAT_ERROR", "Failed to process heartbeat", e.getMessage(), requestId));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AgentResponse>>> getAgents() {
        String requestId = UUID.randomUUID().toString();
        try {
            List<AgentResponse> agents = agentService.getAllAgents();
            return ResponseEntity.ok(ApiResponse.success(agents, requestId));
        } catch (Exception e) {
            log.error("Error fetching agents", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("FETCH_ERROR", "Failed to fetch agents", e.getMessage(), requestId));
        }
    }
}