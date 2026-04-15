package com.braininventory.monitoring.server.module.auth.service.impl;


import com.braininventory.monitoring.common.exception.*;
import com.braininventory.monitoring.server.module.auth.dto.request.LoginRequest;
import com.braininventory.monitoring.server.module.auth.dto.request.OnboardingRequest;
import com.braininventory.monitoring.server.module.auth.dto.request.RegisterRequest;
import com.braininventory.monitoring.server.module.auth.dto.response.LoginResponse;
import com.braininventory.monitoring.server.module.auth.dto.response.RegisterResponse;
import com.braininventory.monitoring.server.module.auth.entity.ResetToken;
import com.braininventory.monitoring.server.module.auth.entity.UserAuth;
import com.braininventory.monitoring.server.module.auth.enums.Role;
import com.braininventory.monitoring.server.module.auth.repository.ResetTokenRepository;
import com.braininventory.monitoring.server.module.auth.repository.UserAuthRepository;
import com.braininventory.monitoring.server.module.auth.security.JwtUtil;
import com.braininventory.monitoring.server.module.auth.service.AuthService;
import com.braininventory.monitoring.server.module.notification.service.NotificationService;
import com.braininventory.monitoring.server.module.organization.entity.Organization;
import com.braininventory.monitoring.server.module.organization.repository.OrganizationRepository;
import com.braininventory.monitoring.server.module.user.entity.Invitation;
import com.braininventory.monitoring.server.module.user.entity.User;
import com.braininventory.monitoring.server.module.user.repository.InvitationRepository;
import com.braininventory.monitoring.server.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserAuthRepository authRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ResetTokenRepository resetTokenRepository;
    private final NotificationService notificationService;
    private final OrganizationRepository organizationRepository;
    private final InvitationRepository invitationRepository;

    // ================= REGISTER =================
    @Transactional
    @Override
    public RegisterResponse register(RegisterRequest request) {
        // 1. Fetch and Verify Invitation via Token
        Invitation invitation = invitationRepository.findByToken(request.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired invitation token"));

        if (invitation.isAccepted() || invitation.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AuthException("This invitation is no longer valid", HttpStatus.GONE);
        }

        // 2. Prevent duplicate email registration
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException("A user with this email is already registered.");
        }

        // 3. Create User & link to the Organization found in the Invitation
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(Role.EMPLOYEE) // Employee role for invited users
                .organization(invitation.getOrganization()) // AUTO-LINKING HAPPENS HERE
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        // 4. Set Password
        UserAuth auth = UserAuth.builder()
                .user(savedUser)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();
        authRepository.save(auth);

        // 5. Consume the Invitation
        invitation.setAccepted(true);
        invitationRepository.save(invitation);

        log.info("Employee registered and linked to Organization: {}", invitation.getOrganization().getName());

        return new RegisterResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getRole().name());
    }

    // ================= LOGIN =================
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        UserAuth auth = authRepository.findById(user.getId())
                .orElseThrow(() -> new InvalidCredentialsException("Auth not found"));

        if (!passwordEncoder.matches(request.getPassword(), auth.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(
                user.getId().toString(),
                user.getEmail(),
                user.getRole().name(),
                user.getOrganization().getId().toString()
        );

        log.info("User {} logged in", user.getEmail());

        return new LoginResponse(token, user.getRole().name(), "Login Successful");
    }

    // ================= LOGOUT =================
    @Override
    public String logout(String email) {
        log.info("User {} logged out", email);
        return "Logout successful";
    }

    // ================= FORGOT PASSWORD =================
    @Override
    public String forgetPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Email does not exist", HttpStatus.NOT_FOUND));

        String token = UUID.randomUUID().toString();

        ResetToken resetToken = ResetToken.builder()
                .userId(user.getId())
                .token(token)
                .expiry(LocalDateTime.now().plusMinutes(30))
                .used(false)
                .build();

        resetTokenRepository.save(resetToken);

        notificationService.sendForgetPasswordEmail(email, token);

        return "Password reset link sent to " + email;
    }

    // ================= RESET PASSWORD =================
    @Override
    public String resetPassword(String token, String newPassword) {

        ResetToken resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthException("Invalid token", HttpStatus.BAD_REQUEST));

        if (resetToken.isUsed()) {
            throw new AuthException("Token already used", HttpStatus.BAD_REQUEST);
        }

        if (resetToken.getExpiry().isBefore(LocalDateTime.now())) {
            throw new AuthException("Token expired", HttpStatus.BAD_REQUEST);
        }

        UserAuth auth = authRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> new AuthException("User not found", HttpStatus.NOT_FOUND));

        auth.setPasswordHash(passwordEncoder.encode(newPassword));
        authRepository.save(auth);

        resetToken.setUsed(true);
        resetTokenRepository.save(resetToken);

        return "Password reset successful";
    }

    @Transactional
    @Override
    public RegisterResponse onboardOrganization(OnboardingRequest request) {
        log.info("Starting onboarding process for admin: {}", request.getAdminDetails().getEmail());

        // 1. Validation: Check if email is already taken
        if (userRepository.findByEmail(request.getAdminDetails().getEmail()).isPresent()) {
            log.warn("Onboarding failed: Email {} already exists", request.getAdminDetails().getEmail());
            // This maps to handleEmailAlreadyRegistered in your GlobalExceptionHandler
            throw new EmailAlreadyRegisteredException("An account with this email already exists.");
        }

        try {
            // 2. Create Organization
            Organization organization = Organization.builder()
                    .name(request.getOrganizationDetails().getName())
                    .timezone(request.getOrganizationDetails().getTimezone())
                    .contactEmail(request.getAdminDetails().getEmail())
                    .isActive(true)
                    .build();
            Organization savedOrg = organizationRepository.save(organization);

            // 3. Create User
            User admin = User.builder()
                    .name(request.getAdminDetails().getName())
                    .email(request.getAdminDetails().getEmail())
                    .role(Role.ADMIN)
                    .organization(savedOrg)
                    .isActive(true)
                    .build();
            User savedAdmin = userRepository.save(admin);

            // 4. Create UserAuth
            UserAuth auth = UserAuth.builder()
                    .user(savedAdmin)
                    .passwordHash(passwordEncoder.encode(request.getAdminDetails().getPassword()))
                    .build();
            authRepository.save(auth);

            log.info("Successfully onboarded organization {} and admin {}", savedOrg.getId(), savedAdmin.getEmail());

            return new RegisterResponse(savedAdmin.getId(), savedAdmin.getEmail(), "ADMIN");

        } catch (Exception e) {
            log.error("Critical error during organization onboarding", e);
            // This maps to handleException (Internal Server Error) in your GlobalExceptionHandler
            throw new RuntimeException("System failed to complete onboarding: " + e.getMessage());
        }
    }
}