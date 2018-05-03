package com.hantong.outbound.processor;


import com.hantong.interfaces.IOutboundProcesser;

public abstract class OutboundProcessor implements IOutboundProcesser{
    public String getName() {
        return name;
    }

    private String name = this.getClass().getName();
}
