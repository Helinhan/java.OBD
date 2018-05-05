package com.hantong.outbound.processor;

import com.hantong.code.ErrorCode;
import com.hantong.communication.Communicate;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SourceReplyOutboundProcessor  extends OutboundProcessor  {
    private Logger LOGGER = Logger.getLogger(SourceReplyOutboundProcessor.class);
    private Long  count = Long.valueOf(1);
    @Override
    public ErrorCode postReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        LOGGER.info("postReceiveMessage");
        count++;
        if (runtimeMessage.getCommunicationSource() == null || runtimeMessage.getContext() == null) {
            return ErrorCode.ServiceNotExist;
        }

        ErrorCode result = ErrorCode.Success;
        Long begin = System.currentTimeMillis();
        try{
            if (runtimeMessage.getCommunicationSource() != null) {
                result = runtimeMessage.getCommunicationSource().sendData(requestMessage, runtimeMessage);
            } else {
                for (Communicate communicate :runtimeMessage.getService().getCommunications()){
                    result = communicate.sendData(requestMessage,runtimeMessage);
                }
            }
        }catch (Exception e) {}

        Long end = System.currentTimeMillis();
        runtimeMessage.addTimestramp(this.getClass().getSimpleName(),begin,end);
        return result;
    }

    @Override
    public Map<String, Map<String, String>> getMonitorData() {
        Map<String, Map<String, String>> monitor = new LinkedHashMap<>();
        Map<String, String> thisMonitor = new LinkedHashMap<>();
        thisMonitor.put("count",count.toString());
        monitor.put("SourceReplyOutboundProcessor",thisMonitor);

        return monitor;
    }
}
