package com.braininventory.monitoring.server.module.activitybackend.service;

import com.braininventory.monitoring.common.dto.request.ActivityRequest;
import com.braininventory.monitoring.common.dto.response.ActivityResponse;

public interface ActivityService {

    ActivityResponse trackActivity(ActivityRequest request);
}
