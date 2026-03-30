package com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request;

import lombok.Data;

@Data
public class ActiveTabDto {
    private String url;
    private String title;
    private long timestamp;
}
