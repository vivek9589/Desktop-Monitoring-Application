package com.braininventory.monitoring.agent;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
        org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
})
@EnableScheduling
public class AgentApplication {
    public static void main(String[] args) {
        // We use SpringApplicationBuilder to disable 'headless' mode
        // This is mandatory for capturing screenshots and AWT/Swing features
        new SpringApplicationBuilder(AgentApplication.class)
                .headless(false)
                .run(args);
    }
}