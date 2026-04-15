package com.braininventory.monitoring.server.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.braininventory.monitoring.server",
        "com.braininventory.monitoring.common"
})
@EnableScheduling
public class ServerApplication {

    public static void main(String[] args) {
        // Required for screenshot + native hooks
        System.setProperty("java.awt.headless", "false");

        SpringApplication.run(ServerApplication.class, args);
    }
}