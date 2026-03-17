package com.braininventory.monitoring.screenshot.monitor.agent.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;
    private T data;
    private ApiError error;
    private Meta meta;

    // Static factory methods
    public static <T> ApiResponse<T> success(T data, String requestId) {
        return ApiResponse.<T>builder()
                .status("success")
                .data(data)
                .meta(new Meta(requestId, Instant.now().toString()))
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message, String details, String requestId) {
        return ApiResponse.<T>builder()
                .status("error")
                .error(new ApiError(code, message, details))
                .meta(new Meta(requestId, Instant.now().toString()))
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiError {
        private String code;
        private String message;
        private String details;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private String requestId;
        private String timestamp;
    }
}