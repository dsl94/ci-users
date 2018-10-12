package com.ciusers.repository;

import com.ciusers.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsernameIgnoreCase(String username);
    User findByEmailIgnoreCase(String email);
}
