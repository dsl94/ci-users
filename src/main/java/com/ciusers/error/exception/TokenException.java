package com.ciusers.error.exception;

import com.ciusers.error.ErrorCode;

public class TokenException extends AbstractException {

    public TokenException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
