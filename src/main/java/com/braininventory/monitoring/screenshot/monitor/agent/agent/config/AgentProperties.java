package com.braininventory.monitoring.screenshot.monitor.agent.agent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "agent")
@Data
public class AgentProperties {

    private String agentId;
    private String storagePath;
    private int screenshotIntervalMinutes;
}