package com.braininventory.monitoring.server.module.auth.service;


import com.braininventory.monitoring.server.module.auth.dto.request.LoginRequest;
import com.braininventory.monitoring.server.module.auth.dto.request.RegisterRequest;
import com.braininventory.monitoring.server.module.auth.dto.response.LoginResponse;
import com.braininventory.monitoring.server.module.auth.dto.response.RegisterResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    RegisterResponse register(RegisterRequest request);
    String logout(String email);
    String forgetPassword(String email);
    String resetPassword(String token, String newPassword);
}