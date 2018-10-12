package com.ciusers.error.exception;

import com.ciusers.error.ErrorCode;

public class RoleException extends AbstractException {

    public RoleException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
