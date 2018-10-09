package com.ciusers.repository;

import com.ciusers.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {

    Role findByRoleIgnoreCase(String role);
}
