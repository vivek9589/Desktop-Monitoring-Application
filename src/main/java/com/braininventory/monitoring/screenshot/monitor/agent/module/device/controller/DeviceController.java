package com.braininventory.monitoring.screenshot.monitor.agent.module.device.controller;


import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.ApiResponse;

import com.braininventory.monitoring.screenshot.monitor.agent.module.device.dto.request.DeviceHeartbeatRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.module.device.dto.response.DeviceResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.module.device.service.DeviceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@Slf4j
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<ApiResponse<DeviceResponse>> createDevice(
            @RequestBody DeviceHeartbeatRequest request,
            HttpServletRequest httpRequest) {

        String requestId = UUID.randomUUID().toString();
        String ipAddress = httpRequest.getRemoteAddr();
        DeviceResponse response = deviceService.createDevice(request, ipAddress);

        return ResponseEntity.ok(ApiResponse.success(response, requestId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DeviceResponse>> updateDevice(
            @PathVariable UUID id,
            @RequestBody DeviceHeartbeatRequest request,
            HttpServletRequest httpRequest) {

        String requestId = UUID.randomUUID().toString();
        String ipAddress = httpRequest.getRemoteAddr();
        DeviceResponse response = deviceService.updateDevice(id, request, ipAddress);

        return ResponseEntity.ok(ApiResponse.success(response, requestId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeviceResponse>>> getAllDevices() {
        String requestId = UUID.randomUUID().toString();
        List<DeviceResponse> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(ApiResponse.success(devices, requestId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DeviceResponse>> getDeviceById(@PathVariable UUID id) {
        String requestId = UUID.randomUUID().toString();
        DeviceResponse device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(ApiResponse.success(device, requestId));
    }
}

