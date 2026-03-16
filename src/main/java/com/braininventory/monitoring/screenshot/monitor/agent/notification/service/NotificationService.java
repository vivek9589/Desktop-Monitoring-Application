package com.braininventory.monitoring.screenshot.monitor.agent.notification.service;

public interface NotificationService {

    String sendForgetPasswordEmail(String toEmail, String resetToken);
}
