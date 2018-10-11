package com.ciusers.error.exception;

import com.ciusers.error.ErrorCode;

public class RoleException extends Exception{

    private ErrorCode errorCode;

    public RoleException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
