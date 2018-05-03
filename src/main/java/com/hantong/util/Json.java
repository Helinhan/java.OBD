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
}
