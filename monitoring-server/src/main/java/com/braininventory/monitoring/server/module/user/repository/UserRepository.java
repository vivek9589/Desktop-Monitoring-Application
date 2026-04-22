package com.braininventory.monitoring.server.module.user.repository;

import com.braininventory.monitoring.server.module.auth.enums.Role;
import com.braininventory.monitoring.server.module.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    List<User> findByIsActiveTrueAndRoleAndOrganizationId(Role role, UUID organizationId);
}
