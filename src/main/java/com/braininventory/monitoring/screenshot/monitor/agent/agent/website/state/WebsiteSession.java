package com.braininventory.monitoring.screenshot.monitor.agent.agent.website.state;

import lombok.Data;

/**
 * Holds current website session state inside agent.
 */
@Data
public class WebsiteSession {

    private String url;
    private String title;
    private long startTime;
    private long endTime;

    public long getDuration() {
        return endTime - startTime;
    }
}