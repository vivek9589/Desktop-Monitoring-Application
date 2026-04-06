package com.braininventory.monitoring.screenshot.monitor.agent.module.auth.service.impl;


import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.InvalidRoleException;
import com.braininventory.monitoring.screenshot.monitor.agent.module.auth.dto.request.LoginRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.module.auth.dto.request.RegisterRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.module.auth.dto.response.LoginResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.module.auth.dto.response.RegisterResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.module.auth.entity.ResetToken;
import com.braininventory.monitoring.screenshot.monitor.agent.module.auth.entity.UserAuth;
import com.braininventory.monitoring.screenshot.monitor.agent.module.auth.enums.Role;
import com.braininventory.monitoring.screenshot.monitor.agent.module.auth.repository.ResetTokenRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.module.auth.repository.UserAuthRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.module.auth.security.JwtUtil;
import com.braininventory.monitoring.screenshot.monitor.agent.module.auth.service.AuthService;
import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.EmailAlreadyRegisteredException;
import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.InvalidCredentialsException;
import com.braininventory.monitoring.screenshot.monitor.agent.module.user.entity.User;
import com.braininventory.monitoring.screenshot.monitor.agent.module.user.repository.UserRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.module.notification.service.NotificationService;
import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.AuthException;
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

    // ================= REGISTER =================
    @Transactional
    @Override
    public RegisterResponse register(RegisterRequest request) {
        // Check for duplicate email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException("Email already exists");
        }

        // Determine role: default to EMPLOYEE if not provided
        Role role;
        if (request.getRole() == null || request.getRole().isBlank()) {
            role = Role.EMPLOYEE;
        } else {
            try {
                role = Role.valueOf(request.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidRoleException("Role must be ADMIN or EMPLOYEE");
            }
        }

        // Create User entity
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(role)
                .isActive(true) // explicitly set active
                .build();

        User savedUser = userRepository.save(user);

        // Create UserAuth entity
        UserAuth auth = UserAuth.builder()
                .user(savedUser)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
               //  .isActive(true) // explicitly set active
                .build();

        authRepository.save(auth);

        // Return response DTO
        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
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
                user.getRole().name()
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
}