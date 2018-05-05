package com.hantong.outbound.strategy;

import com.hantong.code.ErrorCode;
import com.hantong.interfaces.ILifecycle;
import com.hantong.interfaces.IOutbound;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.model.StrategyConfig;
import com.hantong.outbound.chain.OutboundProcessorChain;
import com.hantong.outbound.processor.DefaultOutboundProcessor;
import com.hantong.outbound.processor.SourceReplyOutboundProcessor;
import com.hantong.service.Service;

public abstract class OutboundStrategy implements IOutbound,ILifecycle {
    protected Service service;
    protected StrategyConfig config;

    public String getName() {
        return name;
    }

    protected String name = this.getClass().getName();


    public OutboundProcessorChain getOutboundProcessorChain() {
        return outboundProcessorChain;
    }

    protected OutboundProcessorChain outboundProcessorChain;

    public OutboundStrategy(Service s,StrategyConfig config) {
        this.service = s;
        this.config = config;
        this.outboundProcessorChain = new OutboundProcessorChain();
    }

    @Override
    public ErrorCode lifeStart(){
        for (String processor : config.getProcessor()) {
            if (processor.equals("DefaultProcessor")) {
                DefaultOutboundProcessor processor1 = new DefaultOutboundProcessor();
                outboundProcessorChain.addProcessor(processor1);
            } else if (processor.equals("SourceReplyOutboundProcessor")) {
                SourceReplyOutboundProcessor processor1 = new SourceReplyOutboundProcessor();
                outboundProcessorChain.addProcessor(processor1);
            }
        }
        return ErrorCode.Success;
    }

    @Override
    public ErrorCode lifeStop() {
        this.outboundProcessorChain = null;
        return ErrorCode.Success;
    }

    protected void processOver(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        this.service.onOutboundProcessOver(requestMessage,runtimeMessage);
    }
}
