package com.ciusers.repository;

import com.ciusers.entity.AbstractToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AbstractTokenRepository<T extends AbstractToken> extends JpaRepository<T, UUID> {
}
