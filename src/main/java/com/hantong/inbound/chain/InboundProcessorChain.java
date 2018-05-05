package com.hantong.inbound.chain;

import com.hantong.code.ErrorCode;
import com.hantong.inbound.processor.InboundProcessor;
import com.hantong.interfaces.IInbound;
import com.hantong.interfaces.IMonitor;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.outbound.processor.OutboundProcessor;

import java.util.*;

public class InboundProcessorChain implements IInbound,IMonitor {

    public List<InboundProcessor> getProcessors() {
        return processors;
    }

    List<InboundProcessor> processors = new ArrayList<>();

    public void addProcessor(InboundProcessor processor) {
        for (InboundProcessor pro : processors) {
            if (pro == processor) {
                return;
            }
        }

        this.processors.add(processor);

        return;
    }

    public void delProcessor(InboundProcessor processor) {
        this.processors.remove(processor);
        return;
    }

    @Override
    public ErrorCode onReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        if (this.processors == null) {
            return ErrorCode.Success;
        }

        ErrorCode resultCode = ErrorCode.Success;
        for (InboundProcessor processor : processors) {
            ErrorCode err = processor.receiveMessage(requestMessage,runtimeMessage);
            if (err != ErrorCode.Success) {
                resultCode = err;
            }
        }

        return resultCode;
    }

    @Override
    public Map<String, Map<String, String>> getMonitorData() {
        Map<String, Map<String, String>> monitor = new LinkedHashMap<>();
        Map<String, String> thisMonitor = new LinkedHashMap<>();
        thisMonitor.put("size",String.valueOf(processors.size()));
        monitor.put("InboundProcessorChain",thisMonitor);
        for (InboundProcessor pro : processors) {
            monitor.putAll(pro.getMonitorData());
        }
        return monitor;
    }
}
