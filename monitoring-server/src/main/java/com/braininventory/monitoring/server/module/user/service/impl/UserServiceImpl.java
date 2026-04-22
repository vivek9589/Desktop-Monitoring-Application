package com.braininventory.monitoring.server.module.user.service.impl;

import com.braininventory.monitoring.server.module.auth.enums.Role;
import com.braininventory.monitoring.server.module.user.dto.request.UserResponse;
import com.braininventory.monitoring.server.module.user.entity.User;
import com.braininventory.monitoring.server.module.user.repository.UserRepository;
import com.braininventory.monitoring.server.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getActiveEmployeesByOrganization(UUID organizationId) {
        log.info("Fetching active employees for organization {}", organizationId);

        List<User> users = userRepository.findByIsActiveTrueAndRoleAndOrganizationId(Role.EMPLOYEE, organizationId);

        return users.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .organizationId(user.getOrganization().getId())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

