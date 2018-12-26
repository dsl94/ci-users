package com.ciusers.error.exception;

import com.ci.commons.error.exception.AbstractException;

public class PasswordResetException extends AbstractException {

    public PasswordResetException(String message, String errorCode) {
        super(message, errorCode);
    }
}
