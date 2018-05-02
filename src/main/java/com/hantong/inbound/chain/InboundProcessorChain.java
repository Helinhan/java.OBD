package com.hantong.inbound.chain;

import com.hantong.code.ErrorCode;
import com.hantong.inbound.processor.InboundProcessor;
import com.hantong.interfaces.IInbound;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;

import java.util.List;

public class InboundProcessorChain implements IInbound{

    List<InboundProcessor> processors;

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
