package com.hantong.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {
    public static Json instance = null;

    private ObjectMapper obj = null;

    public Json() {
        this.obj = new ObjectMapper();
        this.obj.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public ObjectMapper getObjectMapper(){
        return this.obj;
    }

    public static Json getInstance() {
        if (Json.instance == null) {
            Json.instance = new Json();
        }

        return Json.instance;
    }

    public String toString(Object o) {
        try {
            return this.obj.writeValueAsString(o);
        } catch (Exception e) {
            return null;
        }
    }

    public<T> T fromString(String s,Class<T> cls) {
        try {
            return this.obj.readValue(s, cls);
        }catch (Exception e) {
            return null;
        }
    }
}
