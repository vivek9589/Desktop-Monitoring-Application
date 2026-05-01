package com.braininventory.monitoring.server.module.user.service;

import com.braininventory.monitoring.common.dto.ApiResponse;
import com.braininventory.monitoring.server.module.user.dto.request.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> getActiveEmployeesByOrganization(UUID organizationId);

    ApiResponse<UserResponse> getUserByEmail(String email, String requestId);

}
