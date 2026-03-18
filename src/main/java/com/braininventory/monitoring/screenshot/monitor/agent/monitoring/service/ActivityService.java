package com.braininventory.monitoring.screenshot.monitor.agent.monitoring.service;

import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.ActivityRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.response.ActivityResponse;

public interface ActivityService {

    ActivityResponse trackActivity(ActivityRequest request);
}
