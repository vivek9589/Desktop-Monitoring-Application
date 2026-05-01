package com.braininventory.monitoring.server.module.user.controller;


import com.braininventory.monitoring.common.dto.ApiResponse;
import com.braininventory.monitoring.common.exception.UserNotFoundException;
import com.braininventory.monitoring.server.module.user.dto.request.UserResponse;
import com.braininventory.monitoring.server.module.user.entity.User;
import com.braininventory.monitoring.server.module.user.repository.UserRepository;
import com.braininventory.monitoring.server.module.user.service.InvitationService;
import com.braininventory.monitoring.server.module.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final InvitationService invitationService;
    private final UserRepository userRepository;
    private final UserService userService;


    @GetMapping("/active/employees/{organizationId}")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getActiveEmployeesByOrganization(
            @PathVariable UUID organizationId,
            HttpServletRequest httpRequest) {
        List<UserResponse> response = userService.getActiveEmployeesByOrganization(organizationId);
        return ResponseEntity.ok(ApiResponse.success(response, httpRequest.getRequestId()));
    }

    @PostMapping("/invite")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> inviteEmployees(
            @RequestBody List<String> emails,
            Principal principal) {

        // Find the admin to get their organization ID
        User admin = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("Admin not found"));

        invitationService.sendBulkInvites(emails, admin.getOrganization().getId());

        return ResponseEntity.ok(ApiResponse.success(
                "Invitations are being processed",
                UUID.randomUUID().toString()
        ));
    }



    @GetMapping("/by-email/{email}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(
            @PathVariable String email,
            HttpServletRequest httpRequest) {
        String requestId = UUID.randomUUID().toString();
        ApiResponse<UserResponse> response = userService.getUserByEmail(email, requestId);
        return ResponseEntity.ok(response);
    }


}
