package com.hantong.message;

import com.hantong.model.Pair;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RuntimeMessage {
    //保留运行时时间戳
    private Map<String,Pair<Long,Long>> timestramp;

    public Map<String, Pair<Long, Long>> getTimestramp() {
        return timestramp;
    }

    public RuntimeMessage() {
        timestramp = new LinkedHashMap<>();
    }

    public void addTimestramp(String name,Long begin,Long end) {
        Pair<Long,Long> time = new Pair<>(begin,end);
        this.timestramp.put(name,time);
    }
}
