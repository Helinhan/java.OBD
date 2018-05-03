package com.hantong.service;

import com.hantong.code.ErrorCode;
import com.hantong.inbound.strategy.BlockInboundStrategy;
import com.hantong.inbound.strategy.DefaultInboundStrategy;
import com.hantong.inbound.strategy.InboundStrategy;
import com.hantong.inbound.strategy.QueueInboundStrategy;
import com.hantong.interfaces.ILifecycle;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.model.Pair;
import com.hantong.model.ServerConfig;
import com.hantong.outbound.strategy.DefaultOutboundStrategy;
import com.hantong.outbound.strategy.OutboundStrategy;
import com.hantong.outbound.strategy.QueueOutboundStrategy;

import java.util.Map;

import static com.hantong.model.StrategyType.Strategy_Block;
import static com.hantong.model.StrategyType.Strategy_Default;
import static com.hantong.model.StrategyType.Strategy_Queue;

public class Service implements ILifecycle {
    private ServerConfig config;

    public Boolean getStart() {
        return start;
    }

    private Boolean start = Boolean.FALSE;
    public Service(ServerConfig config){
        this.config = config;
    }

    public InboundStrategy getInboundStrategy() {
        return inboundStrategy;
    }

    public OutboundStrategy getOutboundStrategy() {
        return outboundStrategy;
    }

    protected InboundStrategy inboundStrategy;
    protected OutboundStrategy outboundStrategy;

    private ErrorCode configInboundStrategy() {
        if (config.getInboundStrategy().getType() == Strategy_Default)
        {
            this.inboundStrategy = new DefaultInboundStrategy(this,config.getInboundStrategy());
        }
        else if (config.getInboundStrategy().getType() == Strategy_Block) {
            this.inboundStrategy = new BlockInboundStrategy(this,config.getInboundStrategy());
        }
        else if (config.getInboundStrategy().getType() == Strategy_Queue) {
            this.inboundStrategy = new QueueInboundStrategy(this,config.getInboundStrategy());
        }
        else {
            return ErrorCode.Failure;
        }

        return this.inboundStrategy.lifeStart();
    }

    private ErrorCode configOutboundStrategy() {
        if (config.getOutboundStrategy().getType() == Strategy_Default) {
            this.outboundStrategy = new DefaultOutboundStrategy(this,config.getOutboundStrategy());
        } else if (config.getOutboundStrategy().getType() == Strategy_Queue) {
            this.outboundStrategy = new QueueOutboundStrategy(this,config.getOutboundStrategy());
        } else {
            return ErrorCode.Failure;
        }

        return this.outboundStrategy.lifeStart();
    }

    @Override
    public ErrorCode lifeStart() {
        if (start == config.getStart()) {
            return ErrorCode.Success;
        }

        this.start = config.getStart();
        ErrorCode result =  configInboundStrategy();
        return configOutboundStrategy();
    }

    @Override
    public ErrorCode lifeStop() {
        return null;
    }

    public ErrorCode InboundProcessOver(RequestMessage requestMessage,RuntimeMessage runtimeMessage){
        if (this.outboundStrategy != null) {
            this.outboundStrategy.doReceiveMessage(requestMessage,runtimeMessage);
        }
        return ErrorCode.Success;
    }
    public ErrorCode OutboundProcessOver(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {

        for(Map.Entry<String,Pair<Long,Long>> entry :runtimeMessage.getTimestramp().entrySet()) {
            Pair<Long,Long> value = entry.getValue();
            System.out.println(String.format("run time:%s %d %d %d",entry.getKey(),value.getKey(),value.getValue(),value.getValue()-value.getKey()));
        }


        return ErrorCode.Success;}
}
