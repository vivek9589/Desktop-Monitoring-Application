package com.braininventory.monitoring.screenshot.monitor.agent.auth.security;


import com.braininventory.monitoring.screenshot.monitor.agent.auth.entity.UserAuth;
import com.braininventory.monitoring.screenshot.monitor.agent.auth.repository.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAuthRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAuth auth = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        log.info("Loaded user {} with role {}", auth.getEmail(), auth.getRole());

        return new CustomUserDetails(
                auth.getEmail(),
                auth.getPasswordHash(),
                auth.getRole().name()
        );
    }
}