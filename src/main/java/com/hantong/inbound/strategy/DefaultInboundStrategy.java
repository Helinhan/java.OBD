package com.hantong.inbound.strategy;

import com.hantong.code.ErrorCode;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.service.Service;

public class DefaultInboundStrategy extends InboundStrategy {
    public DefaultInboundStrategy(Service s) {
        super(s);
    }

    @Override
    public ErrorCode onReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        processOver();
        return ErrorCode.Success;
    }
}
