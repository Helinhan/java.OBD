package com.hantong.outbound.processor;

import com.hantong.code.ErrorCode;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class DefaultOutboundProcessor extends OutboundProcessor {
    private Logger LOGGER = Logger.getLogger(DefaultOutboundProcessor.class);
    private Long count = Long.valueOf(1);
    @Override
    public ErrorCode postReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        count ++;
        LOGGER.info("postReceiveMessage");
        return ErrorCode.Success;
    }

    @Override
    public Map<String, Map<String, String>> getMonitorData() {
        Map<String, Map<String, String>> monitor = new LinkedHashMap<>();
        Map<String, String> thisMonitor = new LinkedHashMap<>();
        thisMonitor.put("count",count.toString());
        monitor.put("DefaultOutboundProcessor",thisMonitor);

        return monitor;
    }
}
