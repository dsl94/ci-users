package com.ciusers.error.exception;

import com.ci.commons.error.exception.AbstractException;

public class TokenException extends AbstractException {

    public TokenException(String message, String errorCode) {
        super(message, errorCode);
    }
}
