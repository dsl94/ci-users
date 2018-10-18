package com.ciusers.controller.dto;

import javax.validation.constraints.NotNull;

public class ResetPasswordDTO {
    @NotNull
    String password;
    @NotNull
    String passwordRetyped;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRetyped() {
        return passwordRetyped;
    }

    public void setPasswordRetyped(String passwordRetyped) {
        this.passwordRetyped = passwordRetyped;
    }
}
