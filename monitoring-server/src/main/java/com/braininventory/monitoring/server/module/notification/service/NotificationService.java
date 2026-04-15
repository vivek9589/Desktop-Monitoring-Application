package com.braininventory.monitoring.server.module.notification.service;

public interface NotificationService {

    String sendForgetPasswordEmail(String toEmail, String resetToken);
    void sendInvitationEmail(String toEmail, String token, String orgName);
}
