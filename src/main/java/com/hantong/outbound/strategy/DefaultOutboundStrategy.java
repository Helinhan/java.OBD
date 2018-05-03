package com.hantong.outbound.strategy;

import com.hantong.code.ErrorCode;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.model.StrategyConfig;
import com.hantong.service.Service;

public class DefaultOutboundStrategy extends OutboundStrategy {
    public DefaultOutboundStrategy(Service s,StrategyConfig config) {
        super(s,config);
    }
    @Override
    public ErrorCode doReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        System.out.println("doReceiveMessage");
        this.processOver(requestMessage,runtimeMessage);
        return ErrorCode.Success;
    }
}
