package com.ciusers.error.exception;


import com.ci.commons.error.exception.AbstractException;

public class RoleException extends AbstractException {

    public RoleException(String message, String errorCode) {
        super(message, errorCode);
    }
}
