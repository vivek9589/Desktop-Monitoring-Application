package com.braininventory.monitoring.screenshot.monitor.agent.module.notification.service;

public interface NotificationService {

    String sendForgetPasswordEmail(String toEmail, String resetToken);
}
