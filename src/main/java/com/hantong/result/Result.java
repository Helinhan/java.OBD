package com.hantong.result;

import com.hantong.code.ErrorCode;

import java.util.HashMap;
import java.util.Map;

public class Result extends HashMap<Object,Object> {

    public Result(){
        this.setTimestamp(System.currentTimeMillis());
        this.setErrorCode(ErrorCode.Success);
    }

    public Result(ErrorCode errorCode){
        this.setErrorCode(errorCode);
        this.setTimestamp(System.currentTimeMillis());
    }

    public void setTimestamp(long timestamp) {
        this.put("timestamp",timestamp);
    }

    public void setErrorCode(ErrorCode errorCode) {
        if (errorCode != ErrorCode.Success) {
            this.put("error", errorCode.toString());
        }
        this.put("message",errorCode.getMessage());
        this.put("code",errorCode.getCode());
    }

    public static Result from(Map info){
        Result result  = new Result();
        result.putAll(info);
        return result;
    }

    public static Result from(Object key, Object value) {
        Result result  = new Result();
        result.put(key,value);
        return result;
    }
}
