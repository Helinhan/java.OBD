package com.hantong.exception;

import com.hantong.code.ErrorCode;

public class ErrorCodeException extends Exception {
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    private ErrorCode errorCode;
    public ErrorCodeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
