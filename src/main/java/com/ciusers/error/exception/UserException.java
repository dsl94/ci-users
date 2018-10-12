package com.ciusers.error.exception;

import com.ciusers.error.ErrorCode;

public class UserException extends AbstractException {

    public UserException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
