package com.ciusers.entity;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class PasswordResetToken extends AbstractToken{

    public PasswordResetToken() {
        super();
    }

    public PasswordResetToken(Date valid) {
        super(valid);
    }
}
