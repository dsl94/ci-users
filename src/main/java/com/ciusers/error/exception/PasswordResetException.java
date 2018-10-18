package com.ciusers.error.exception;

import com.ciusers.error.ErrorCode;

public class PasswordResetException extends AbstractException {

    public PasswordResetException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
