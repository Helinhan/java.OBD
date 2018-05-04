package com.hantong.inbound.strategy;

import com.hantong.HantongApplication;
import com.hantong.code.ErrorCode;
import com.hantong.inbound.chain.InboundProcessorChain;
import com.hantong.inbound.processor.DbPersisProcessor;
import com.hantong.inbound.processor.DefaultInboundProcessor;
import com.hantong.interfaces.IInbound;
import com.hantong.interfaces.ILifecycle;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.model.StrategyConfig;
import com.hantong.service.Service;

public abstract class InboundStrategy implements IInbound,ILifecycle {
    protected Service service;
    protected StrategyConfig config;
    protected InboundProcessorChain inboundProcessorChain;

    public String getName() {
        return name;
    }

    protected String name = this.getClass().getName();

    public InboundProcessorChain getInboundProcessorChain() {
        return inboundProcessorChain;
    }

    public InboundStrategy(Service s,StrategyConfig config) {
        this.service = s;
        this.config = config;
        this.inboundProcessorChain = new InboundProcessorChain();
    }

    @Override
    public ErrorCode onReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        return null;
    }

    protected void processOver(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        this.service.onInboundProcessOver(requestMessage,runtimeMessage);
    }

    @Override
    public ErrorCode lifeStart() {
        for (String processor : config.getProcessor()) {
            if (processor.equals("DbPersisProcessor")) {
                DbPersisProcessor processor1 = (DbPersisProcessor)HantongApplication.getApplicationContext().getBean("DbPersisProcessor");
                inboundProcessorChain.addProcessor(processor1);
            } else if (processor.equals("DefaultProcessor")) {
                DefaultInboundProcessor processor1 = new DefaultInboundProcessor();
                inboundProcessorChain.addProcessor(processor1);
            }
        }
        return ErrorCode.Success;
    }

    @Override
    public ErrorCode lifeStop() {
        this.inboundProcessorChain = null;
        return ErrorCode.Success;
    }
}
