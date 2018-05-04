package com.hantong.code;

public enum ErrorCode {
    //通用
    Success          (0,     "OK"),
    Failure          (1,     "runtime failure"),
    ErrParam         (2,     "the param is error"),
    Unauthorized     (401,   "Full authentication is required to access this resource"),

    //用户
    UserAlreadyExist (1001,  "user already exist"),
    UserNotExist     (1002,  "user is not exist"),

    //服务
    ServiceNotExist  (2002,  "service is not exist"),
    ServiceStartErr  (2003,  "service startup error");

    ErrorCode(long id, String msg){
        this.code =id;
        this.message = msg;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private long code;
    private String message;
}
