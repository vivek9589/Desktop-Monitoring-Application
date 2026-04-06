package com.braininventory.monitoring.screenshot.monitor.agent.module.auth.service.impl;


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
import com.braininventory.monitoring.screenshot.monitor.agent.notification.service.NotificationService;
import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserAuthRepository authRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ResetTokenRepository resetTokenRepository;
    private final NotificationService notificationService;




    /**
     * Registers a new user (Admin or Employee).
     * Validates duplicate email and hashes password before saving.
     */
    /**
     * Registers a new user (Admin or Employee).
     * Validates duplicate email and hashes password before saving.
     */
    public RegisterResponse register(RegisterRequest request) {
        // Check if email already exists
        if (authRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Registration failed: Email {} already registered", request.getEmail());
            throw new EmailAlreadyRegisteredException("Email already registered: " + request.getEmail());
        }

        // Build new user entity
        UserAuth user = UserAuth.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword())) // ✅ Fixed
                .role(Role.valueOf(request.getRole().toUpperCase()))
                .active(true)
                .build();

        // Save to DB
        UserAuth saved = authRepository.save(user);

        log.info("New user registered: {} with role {}", saved.getEmail(), saved.getRole());

        return new RegisterResponse(saved.getId(), saved.getEmail(), saved.getRole().name());
    }



    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()
                    )
            );

            UserAuth auth = authRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

            String token = jwtUtil.generateToken(
                    auth.getId().toString(),
                    auth.getEmail(),
                    auth.getRole().name()
            );

            log.info("User {} logged in successfully with role {}", auth.getEmail(), auth.getRole());

            return new LoginResponse(token, auth.getRole().name(), "Login Successful");

        } catch (Exception e) {
            log.error("Login failed for user {}: {}", request.getEmail(), e.getMessage());
            throw new InvalidCredentialsException("Login failed. Please check your credentials.");
        }
    }

    @Override
    public String logout(String email) {
        // In JWT-based auth, logout is usually handled client-side by discarding the token.
        log.info("User {} logged out", email);
        return "Logout successful";
    }

    @Override
    public String forgetPassword(String email) {

        // TODO: Generate reset token, send email (stubbed for now)
        // 1. Check if user exists
        UserAuth user = authRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Email does not exist", HttpStatus.NOT_FOUND));

        // 2. Create reset token
        String token = UUID.randomUUID().toString();
        ResetToken resetToken = ResetToken.builder()
                .userId(user.getId())
                .token(token)
                .expiry(LocalDateTime.now().plusMinutes(30))
                .used(false)
                .build();

        try {
            resetTokenRepository.save(resetToken);
            log.info("Reset token generated for user {}", email);
        } catch (Exception e) {
            log.error("Database error while saving reset token", e);
            throw new AuthException("Failed to initiate password reset. Try again.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 3. Send email
        try {
            notificationService.sendForgetPasswordEmail(email, token);
            log.info("Password reset email sent to {}", email);
        } catch (Exception e) {
            log.error("Email notification failed for {}", email, e);
            throw new AuthException("Failed to send reset email. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
        }


        log.info("Password reset requested for {}", email);
        return "Password reset link sent to " + email;
    }

    @Override
    public String resetPassword(String token, String newPassword) {
        // 1. Find reset token
        ResetToken resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthException("Invalid reset token", HttpStatus.BAD_REQUEST));

        // 2. Validate token
        if (resetToken.isUsed()) {
            throw new AuthException("Reset token already used", HttpStatus.BAD_REQUEST);
        }
        if (resetToken.getExpiry().isBefore(LocalDateTime.now())) {
            throw new AuthException("Reset token expired", HttpStatus.BAD_REQUEST);
        }

        // 3. Fetch user
        UserAuth user = authRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> new AuthException("User not found", HttpStatus.NOT_FOUND));

        // 4. Update password (always hash!)
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        authRepository.save(user);

        // 5. Mark token as used
        resetToken.setUsed(true);
        resetTokenRepository.save(resetToken);

        log.info("Password successfully reset for user {}", user.getEmail());
        return "Password reset successful for " + user.getEmail();
    }

}