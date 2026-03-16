package com.braininventory.monitoring.screenshot.monitor.agent.controller;

import com.braininventory.monitoring.screenshot.monitor.agent.dto.request.AgentHeartbeatRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.dto.response.AgentResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.dto.response.ApiResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.service.AgentService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
public class AgentController {

    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);

    @Autowired
    private AgentService agentService;

    @PostMapping("/heartbeat")
    public ResponseEntity<ApiResponse<AgentResponse>> heartbeat(
            @RequestBody AgentHeartbeatRequest request,
            HttpServletRequest httpRequest) {
        try {
            String ipAddress = httpRequest.getRemoteAddr();
            AgentResponse response = agentService.registerOrUpdateAgent(request, ipAddress);
            return ResponseEntity.ok(ApiResponse.success(response, "Agent heartbeat processed successfully"));
        } catch (Exception e) {
            logger.error("Error processing heartbeat", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to process heartbeat"));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AgentResponse>>> getAgents() {
        try {
            List<AgentResponse> agents = agentService.getAllAgents();
            return ResponseEntity.ok(ApiResponse.success(agents, "Agents fetched successfully"));
        } catch (Exception e) {
            logger.error("Error fetching agents", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch agents"));
        }
    }
}