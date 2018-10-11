package com.ciusers.repository;

import com.ciusers.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsernameIgnoreCase(String username);
    User findByEmailIgnoreCase(String email);
}
