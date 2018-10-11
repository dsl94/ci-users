package com.ciusers.error;

public class ErrorMessage {
    private String message;
    private ErrorCode errorCode;

    public ErrorMessage(String message, ErrorCode errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public ErrorMessage() {
        this.message = "Something went wrong";
        this.errorCode = ErrorCode.GENERAL_ERROR;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
