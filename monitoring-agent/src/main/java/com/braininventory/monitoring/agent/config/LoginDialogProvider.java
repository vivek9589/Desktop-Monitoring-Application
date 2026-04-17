package com.braininventory.monitoring.agent.config;


import com.braininventory.monitoring.agent.config.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
@RequiredArgsConstructor
public class LoginDialogProvider {

    private final AuthService authService;

    public void showLoginDialog() {
        // Use EventQueue to ensure UI runs on the correct thread
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            JTextField emailField = new JTextField(20);
            JPasswordField passwordField = new JPasswordField(20);

            JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
            panel.add(new JLabel("Email:"));
            panel.add(emailField);
            panel.add(new JLabel("Password:"));
            panel.add(passwordField);

            int result = JOptionPane.showConfirmDialog(null, panel,
                    "Agent Login Required", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (authService.authenticate(email, password)) {
                    JOptionPane.showMessageDialog(null, "Login Successful! Monitoring started.");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Credentials. Please try again.",
                            "Login Error", JOptionPane.ERROR_MESSAGE);
                    showLoginDialog(); // Recursive call to let them try again
                }
            } else {
                // If they click cancel, the app stays unauthenticated
                System.out.println("User cancelled login.");
            }
        });
    }
}