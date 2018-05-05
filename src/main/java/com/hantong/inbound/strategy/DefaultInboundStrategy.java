package com.hantong.inbound.strategy;

import com.hantong.code.ErrorCode;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.model.StrategyConfig;
import com.hantong.service.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultInboundStrategy extends InboundStrategy {
    public DefaultInboundStrategy(Service s,StrategyConfig config) {
        super(s,config);
    }

    @Override
    public ErrorCode onReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        System.out.println("onReceiveMessage");
        processOver(requestMessage,runtimeMessage);
        return ErrorCode.Success;
    }

    @Override
    public Map<String, Map<String, String>> getMonitorData() {
        Map<String, Map<String, String>> monitor = new LinkedHashMap<>();
        Map<String, String> thisMonitor = new LinkedHashMap<>();
        thisMonitor.put("--","--");
        monitor.put("DefaultInboundStrategy",thisMonitor);

        return monitor;
    }
}
