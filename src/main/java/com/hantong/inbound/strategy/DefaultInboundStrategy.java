package com.hantong.inbound.strategy;

import com.hantong.code.ErrorCode;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.model.StrategyConfig;
import com.hantong.service.Service;

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
}
