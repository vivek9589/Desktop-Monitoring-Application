package com.braininventory.monitoring.screenshot.monitor.agent.auth.controller;



import com.braininventory.monitoring.screenshot.monitor.agent.auth.dto.request.LoginRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.auth.dto.request.RegisterRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.auth.dto.response.LoginResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.auth.dto.response.RegisterResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.auth.service.AuthService;
import com.braininventory.monitoring.screenshot.monitor.agent.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint for user registration.
     * Accepts email, password, and role.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        String requestId = UUID.randomUUID().toString();
        try {
            RegisterResponse response = authService.register(request);
            return ResponseEntity.ok(ApiResponse.success(response, requestId));
        } catch (Exception e) {
            log.error("Error during registration for {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("REGISTRATION_ERROR", "Failed to register user", e.getMessage(), requestId));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        ApiResponse<LoginResponse> response = ApiResponse.success(loginResponse, "req-" + System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestParam String email) {
        String result = authService.logout(email);
        return ResponseEntity.ok(ApiResponse.success(result, "req-" + System.currentTimeMillis()));
    }

    @PostMapping("/forget-password")
    public ResponseEntity<ApiResponse<String>> forgetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String result = authService.forgetPassword(email);
        return ResponseEntity.ok(ApiResponse.success(result, "req-" + System.currentTimeMillis()));
    }

    @PostMapping("/reset-password")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        String result = authService.resetPassword(token, newPassword);

        return ResponseEntity.ok(ApiResponse.success(result, "req-" + System.currentTimeMillis()));
    }
}