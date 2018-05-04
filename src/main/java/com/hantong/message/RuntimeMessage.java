package com.hantong.message;

import com.hantong.code.ErrorCode;
import com.hantong.interfaces.ICommunication;
import com.hantong.model.Pair;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RuntimeMessage {
    //保留运行时时间戳
    private Map<String,Pair<Long,Long>> timestramp;
    private ICommunication communicationSource;

    public ErrorCode getResult() {
        return result;
    }

    public void setResult(ErrorCode result) {
        this.result = result;
    }

    private ErrorCode result = ErrorCode.Success;

    public ICommunication getCommunicationSource() {
        return communicationSource;
    }

    public void setCommunicationSource(ICommunication communicationSource) {
        this.communicationSource = communicationSource;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    private Object context;

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
