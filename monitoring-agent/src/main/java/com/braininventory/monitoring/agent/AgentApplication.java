package com.braininventory.monitoring.agent;

import com.braininventory.monitoring.agent.config.LoginDialogProvider;
import com.braininventory.monitoring.agent.config.TokenManager;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
        org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
})
@EnableScheduling
public class AgentApplication {
    public static void main(String[] args) {
        // Disable headless mode to allow Swing/AWT popups
        ConfigurableApplicationContext context = new SpringApplicationBuilder(AgentApplication.class)
                .headless(false)
                .run(args);

        TokenManager tokenManager = context.getBean(TokenManager.class);
        LoginDialogProvider dialogProvider = context.getBean(LoginDialogProvider.class);

        // Check if we need to show the login popup
        if (!tokenManager.isAuthenticated()) {
            dialogProvider.showLoginDialog();
        }
    }
}