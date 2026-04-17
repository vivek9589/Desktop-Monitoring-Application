package com.braininventory.monitoring.agent.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF (Cross-Site Request Forgery) for local API calls
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Configure which paths are public and which are private
                .authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll()
//                        .requestMatchers("/auth/**").permitAll() // ALLOW THIS without login
//                        .anyRequest().authenticated()            // PROTECT everything else
                );

        return http.build();
    }
}
