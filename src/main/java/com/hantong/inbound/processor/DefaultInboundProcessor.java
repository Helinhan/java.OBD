package com.hantong.inbound.processor;

import com.hantong.code.ErrorCode;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultInboundProcessor extends InboundProcessor{
    @Override
    public ErrorCode receiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        return ErrorCode.Success;
    }

    @Override
    public Map<String, Map<String, String>> getMonitorData() {
        Map<String, Map<String, String>> monitor = new LinkedHashMap<>();
        Map<String, String> thisMonitor = new LinkedHashMap<>();
        thisMonitor.put("--","--");
        monitor.put("DefaultInboundProcessor",thisMonitor);

        return monitor;
    }
}
