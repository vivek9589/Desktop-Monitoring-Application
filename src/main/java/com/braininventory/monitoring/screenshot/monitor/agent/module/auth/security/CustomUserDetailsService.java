package com.braininventory.monitoring.screenshot.monitor.agent.module.auth.security;


import com.braininventory.monitoring.screenshot.monitor.agent.module.user.entity.User;
import com.braininventory.monitoring.screenshot.monitor.agent.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        log.info("Loaded user {} with role {}", user.getEmail(), user.getRole());

        return new CustomUserDetails(
                user.getEmail(),
                user.getUserAuth().getPasswordHash(),
                user.getRole().name()
        );
    }
}