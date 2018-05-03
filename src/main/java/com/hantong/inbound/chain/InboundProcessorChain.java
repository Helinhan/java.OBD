package com.hantong.inbound.chain;

import com.hantong.code.ErrorCode;
import com.hantong.inbound.processor.InboundProcessor;
import com.hantong.interfaces.IInbound;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;

import java.util.ArrayList;
import java.util.List;

public class InboundProcessorChain implements IInbound{

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
}
