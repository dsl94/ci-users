package com.ciusers.error.exception;

import com.ci.commons.error.exception.AbstractException;

public class UserException extends AbstractException {

    public UserException(String message, String errorCode) {
        super(message, errorCode);
    }
}
