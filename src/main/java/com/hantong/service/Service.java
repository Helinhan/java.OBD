package com.hantong.service;

import com.hantong.code.ErrorCode;
import com.hantong.inbound.strategy.InboundStrategy;

public abstract class Service {
    public Service(){
    }
    protected InboundStrategy inboundStrategy;

    public ErrorCode InboundProcessOver(){
        return ErrorCode.Success;
    }
}
