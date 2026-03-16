package com.braininventory.monitoring.screenshot.monitor.agent.notification.service.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.auth.entity.UserAuth;
import com.braininventory.monitoring.screenshot.monitor.agent.auth.repository.UserAuthRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.notification.service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final UserAuthRepository userAuthRepository;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public String sendForgetPasswordEmail(String toEmail, String resetToken) {
        String resetLink = "http://localhost:9090/api/auth/reset-password?token=" + resetToken;

        UserAuth user = userAuthRepository.findByEmail(toEmail).orElse(null);

        Context context = new Context();
        context.setVariable("username", user != null ? user.getEmail() : "User");
        context.setVariable("resetLink", resetLink);
        context.setVariable("expiryMinutes", 30);

        String body = templateEngine.process("forget-password", context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request");
            helper.setText(body, true); // true = HTML
            mailSender.send(message);

            log.info("Password reset email successfully sent to {}", toEmail);
            return "Password reset email sent successfully to " + toEmail;

        } catch (MessagingException e) {
            log.error("Failed to send password reset email to {}", toEmail, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}