package com.hantong.message;

import com.hantong.code.ErrorCode;
import com.hantong.interfaces.ICommunication;
import com.hantong.model.Pair;
import com.hantong.service.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RuntimeMessage {
    //保留运行时时间戳
    private Map<String,Pair<Long,Long>> timestramp;
    private ICommunication communicationSource;
    private Service service;
    private ErrorCode result = ErrorCode.Success;
    private Object context;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public ErrorCode getResult() {
        return result;
    }
    public void setResult(ErrorCode result) {
        this.result = result;
    }
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
