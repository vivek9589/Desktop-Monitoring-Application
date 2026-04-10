package com.braininventory.monitoring.common.exception;

public class DeviceAlreadyExistsException extends RuntimeException {
    public DeviceAlreadyExistsException(String message) {
        super(message);
    }
}