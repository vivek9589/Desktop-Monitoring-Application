package com.braininventory.monitoring.screenshot.monitor.agent.common.exception;

public class DeviceAlreadyExistsException extends RuntimeException {
    public DeviceAlreadyExistsException(String message) {
        super(message);
    }
}