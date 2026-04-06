package com.braininventory.monitoring.screenshot.monitor.agent.notification.service.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.module.auth.entity.UserAuth;
import com.braininventory.monitoring.screenshot.monitor.agent.module.user.entity.User;
import com.braininventory.monitoring.screenshot.monitor.agent.module.user.repository.UserRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.notification.service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.reset-password.path}")
    private String resetPasswordPath;

    @Override
    public String sendForgetPasswordEmail(String toEmail, String resetToken) {
        User user = userRepository.findByEmail(toEmail).orElse(null);

        String resetLink = baseUrl + resetPasswordPath + resetToken;

        Context context = new Context();
        context.setVariable("username", user.getEmail() != null ? user.getEmail() : "User");
        context.setVariable("resetLink", resetLink);
        context.setVariable("expiryMinutes", 30);

        String body = templateEngine.process("forget-password", context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request");
            helper.setText(body, true);
            mailSender.send(message);

            log.info("Password reset email successfully sent to {}", toEmail);
            return "Password reset email sent successfully to " + toEmail;

        } catch (MessagingException e) {
            log.error("Failed to send password reset email to {}", toEmail, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}