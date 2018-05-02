package com.hantong.inbound.processor;

import com.hantong.code.ErrorCode;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;

public class DefaultInboundProcessor extends InboundProcessor{
    @Override
    public ErrorCode receiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        return ErrorCode.Success;
    }
}
