package com.ciusers.repository;

import com.ciusers.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends AbstractTokenRepository<PasswordResetToken>{

    PasswordResetToken findByToken(String token);
}
