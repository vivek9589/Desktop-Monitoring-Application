package com.braininventory.monitoring.screenshot.monitor.agent.common.exception;


import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        logger.error("Unexpected error occurred", ex);
        String requestId = UUID.randomUUID().toString();

        ApiResponse<Object> response = ApiResponse.error(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                ex.getMessage(),
                requestId
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleEntityNotFound(EntityNotFoundException ex) {
        logger.warn("Entity not found: {}", ex.getMessage());
        String requestId = UUID.randomUUID().toString();

        ApiResponse<Object> response = ApiResponse.error(
                "ENTITY_NOT_FOUND",
                "Requested entity not found",
                ex.getMessage(),
                requestId
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }



    // Handle resource not found exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        String requestId = UUID.randomUUID().toString();
        ApiResponse<Object> response = ApiResponse.error(
                "RESOURCE_NOT_FOUND",
                ex.getMessage(),
                "The requested resource could not be found",
                requestId
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String requestId = UUID.randomUUID().toString();
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        ApiResponse<Object> response = ApiResponse.error(
                "VALIDATION_ERROR",
                "Invalid input data",
                errorMessage,
                requestId
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle invalid credentials
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidCredentials(InvalidCredentialsException ex) {
        String requestId = UUID.randomUUID().toString();
        ApiResponse<Object> response = ApiResponse.error(
                "INVALID_CREDENTIALS",
                "Login failed",
                ex.getMessage(),
                requestId
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        String requestId = UUID.randomUUID().toString();
        ApiResponse<Object> response = ApiResponse.error(
                "INVALID_CREDENTIALS",
                "Login failed",
                "Invalid username or password",
                requestId
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // Handle email already registered
    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ApiResponse<Object>> handleEmailAlreadyRegistered(EmailAlreadyRegisteredException ex) {
        String requestId = UUID.randomUUID().toString();
        ApiResponse<Object> response = ApiResponse.error(
                "EMAIL_ALREADY_REGISTERED",
                "Registration failed",
                ex.getMessage(),
                requestId
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Handle phone number already registered
    @ExceptionHandler(PhoneNumberAlreadyRegisteredException.class)
    public ResponseEntity<ApiResponse<Object>> handlePhoneNumberAlreadyRegistered(PhoneNumberAlreadyRegisteredException ex) {
        String requestId = UUID.randomUUID().toString();
        ApiResponse<Object> response = ApiResponse.error(
                "PHONE_ALREADY_REGISTERED",
                "Registration failed",
                ex.getMessage(),
                requestId
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    @ExceptionHandler(ScreenshotUploadException.class)
    public ResponseEntity<ApiResponse<Object>> handleScreenshotUpload(ScreenshotUploadException ex) {
        String requestId = UUID.randomUUID().toString();
        ApiResponse<Object> response = ApiResponse.error(
                "SCREENSHOT_UPLOAD_ERROR",
                "Failed to upload screenshot",
                ex.getMessage(),
                requestId
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthException(AuthException ex) {
        String requestId = UUID.randomUUID().toString();
        ApiResponse<Object> response = ApiResponse.error(
                ex.getStatus().name(),
                "Authentication error",
                ex.getMessage(),
                requestId
        );
        return ResponseEntity.status(ex.getStatus()).body(response);
    }


    /**
     * Handle custom AgentException.
     * This is thrown for agent-specific errors in monitoring logic.
     * Returns a structured ApiResponse with error details and metadata.
     */
    @ExceptionHandler(AgentException.class)
    public ResponseEntity<ApiResponse<Object>> handleAgentException(AgentException ex) {
        logger.error("Agent error occurred: {}", ex.getMessage());
        String requestId = UUID.randomUUID().toString();

        ApiResponse<Object> response = ApiResponse.error(
                "AGENT_ERROR",
                "Agent processing failed",
                ex.getMessage(),
                requestId
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    @ExceptionHandler(DeviceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleDeviceAlreadyExists(DeviceAlreadyExistsException ex) {
        String requestId = UUID.randomUUID().toString();
        logger.warn("Device already exists: {}", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.error(
                "DEVICE_ALREADY_EXISTS",
                "Device creation failed",
                ex.getMessage(),
                requestId
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleDeviceNotFound(DeviceNotFoundException ex) {
        String requestId = UUID.randomUUID().toString();
        logger.warn("Device not found: {}", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.error(
                "DEVICE_NOT_FOUND",
                "Requested device not found",
                ex.getMessage(),
                requestId
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleTaskNotFound(TaskNotFoundException ex) {
        String requestId = UUID.randomUUID().toString();
        ApiResponse<Object> response = ApiResponse.error(
                "TASK_NOT_FOUND",
                "Requested task not found",
                ex.getMessage(),
                requestId
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleProjectNotFound(ProjectNotFoundException ex) {
        String requestId = UUID.randomUUID().toString();
        ApiResponse<Object> response = ApiResponse.error(
                "PROJECT_NOT_FOUND",
                "Requested project not found",
                ex.getMessage(),
                requestId
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNotFoundException ex) {
        String requestId = UUID.randomUUID().toString();
        ApiResponse<Object> response = ApiResponse.error(
                "USER_NOT_FOUND",
                "Requested user not found",
                ex.getMessage(),
                requestId
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    @ExceptionHandler(OrganizationNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleOrganizationNotFound(OrganizationNotFoundException ex) {
        String requestId = UUID.randomUUID().toString();
        ApiResponse<Object> response = ApiResponse.error(
                "ORGANIZATION_NOT_FOUND",
                "Requested organization not found",
                ex.getMessage(),
                requestId
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


}