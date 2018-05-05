package com.hantong.outbound.chain;

import com.hantong.code.ErrorCode;
import com.hantong.inbound.processor.InboundProcessor;
import com.hantong.interfaces.IMonitor;
import com.hantong.interfaces.IOutbound;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.outbound.processor.OutboundProcessor;

import java.util.*;

public class OutboundProcessorChain implements IOutbound,IMonitor {
    public List<OutboundProcessor> getProcessors() {
        return processors;
    }

    List<OutboundProcessor> processors = new ArrayList<>();

    @Override
    public ErrorCode doReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        if (null == processors) {
            return ErrorCode.Success;
        }

        ErrorCode resultCode = ErrorCode.Success;
        for (OutboundProcessor processor : processors) {
            ErrorCode result = processor.postReceiveMessage(requestMessage,runtimeMessage);
            if (result != ErrorCode.Success) {
                resultCode = result;
            }
        }

        return resultCode;
    }

    public void addProcessor(OutboundProcessor processor) {
        for (OutboundProcessor pro : processors) {
            if (pro == processor) {
                return;
            }
        }

        this.processors.add(processor);

        return;
    }

    public void delProcessor(OutboundProcessor processor) {
        this.processors.remove(processor);
        return;
    }

    @Override
    public Map<String, Map<String, String>> getMonitorData() {
        Map<String, Map<String, String>> monitor = new LinkedHashMap<>();
        Map<String, String> thisMonitor = new LinkedHashMap<>();
        thisMonitor.put("size",String.valueOf(processors.size()));
        monitor.put("OutboundProcessorChain",thisMonitor);
        for (OutboundProcessor pro : processors) {
            monitor.putAll(pro.getMonitorData());
        }
        return monitor;
    }
}
