package com.braininventory.monitoring.screenshot.monitor.agent.module.user.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.module.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
