package com.hantong.inbound.processor;

import com.hantong.interfaces.IInboundProcesser;

public abstract class InboundProcessor implements IInboundProcesser {
    public String getName() {
        return name;
    }

    private String name = this.getClass().getName();
}
