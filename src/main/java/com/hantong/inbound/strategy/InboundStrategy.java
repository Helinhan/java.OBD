package com.hantong.inbound.strategy;

import com.hantong.code.ErrorCode;
import com.hantong.interfaces.IInbound;
import com.hantong.interfaces.ILifecycle;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.service.Service;

public abstract class InboundStrategy implements IInbound,ILifecycle {
    protected Service service;

    public InboundStrategy(Service s) {
        this.service = s;
    }

    @Override
    public ErrorCode onReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        return null;
    }

    @Override
    public ErrorCode lifeStart() {
        return ErrorCode.Success;
    }

    @Override
    public ErrorCode lifeStop() {
        return ErrorCode.Success;
    }

    protected void processOver() {
        this.service.InboundProcessOver();
    }
}
