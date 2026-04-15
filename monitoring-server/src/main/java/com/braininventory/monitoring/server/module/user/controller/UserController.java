package com.braininventory.monitoring.server.module.user.controller;


import com.braininventory.monitoring.common.dto.ApiResponse;
import com.braininventory.monitoring.common.exception.UserNotFoundException;
import com.braininventory.monitoring.server.module.user.entity.User;
import com.braininventory.monitoring.server.module.user.repository.UserRepository;
import com.braininventory.monitoring.server.module.user.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final InvitationService invitationService;
    private final UserRepository userRepository;

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
}
