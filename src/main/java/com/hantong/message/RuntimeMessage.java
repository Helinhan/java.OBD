package com.hantong.message;

import com.hantong.model.Pair;

import java.util.HashMap;
import java.util.Map;

public class RuntimeMessage {
    //保留运行时时间戳
    private Map<String,Pair<Long,Long>> timestramp;

    public RuntimeMessage() {
        timestramp = new HashMap<>();
    }
}
