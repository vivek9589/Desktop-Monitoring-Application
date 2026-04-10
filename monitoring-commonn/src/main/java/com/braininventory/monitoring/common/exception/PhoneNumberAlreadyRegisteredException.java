package com.braininventory.monitoring.common.exception;

public class PhoneNumberAlreadyRegisteredException extends RuntimeException {
    public PhoneNumberAlreadyRegisteredException(String message) {
        super(message);
    }
}
