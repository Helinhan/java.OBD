package com.hantong.service;

import codec.EncoderDecoder;
import codec.StandardEncoderDeCoder;
import com.hantong.code.ErrorCode;
import com.hantong.communication.Communicate;
import com.hantong.communication.component.SocketCommunicate;
import com.hantong.inbound.strategy.BlockInboundStrategy;
import com.hantong.inbound.strategy.DefaultInboundStrategy;
import com.hantong.inbound.strategy.InboundStrategy;
import com.hantong.inbound.strategy.QueueInboundStrategy;
import com.hantong.interfaces.ILifecycle;
import com.hantong.interfaces.ICommunication;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.model.CommunicationConfig;
import com.hantong.model.Pair;
import com.hantong.model.ServerConfig;
import com.hantong.outbound.strategy.DefaultOutboundStrategy;
import com.hantong.outbound.strategy.OutboundStrategy;
import com.hantong.outbound.strategy.QueueOutboundStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hantong.model.StrategyType.Strategy_Block;
import static com.hantong.model.StrategyType.Strategy_Default;
import static com.hantong.model.StrategyType.Strategy_Queue;

public class Service implements ILifecycle {
    private ServerConfig config;
    private Boolean start = Boolean.FALSE;
    protected InboundStrategy inboundStrategy;
    protected OutboundStrategy outboundStrategy;
    protected List<Communicate>  communications;
    protected EncoderDecoder encoderDecoder;

    public Service(ServerConfig config){
        this.config = config;
        this.communications = new ArrayList<>();
    }

    public Boolean getStart() {
        return start;
    }

    public InboundStrategy getInboundStrategy() {
        return inboundStrategy;
    }

    public OutboundStrategy getOutboundStrategy() {
        return outboundStrategy;
    }

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

    private ErrorCode configCommunication() {
        for (CommunicationConfig conf:config.getCommunicationConfigs())
        {
            if (conf.getType() == CommunicationConfig.CommunicationType.Socket) {
                Communicate communication = new SocketCommunicate(conf.getSocketCfg(),this.encoderDecoder,this);
                communication.lifeStart();
                this.communications.add(communication);
            }
        }

        return ErrorCode.Success;
    }

    private ErrorCode configEncoderDecoder(){
        if (this.config.getCodec().equals("StandardEncoderDeCoder")) {
            this.encoderDecoder = new StandardEncoderDeCoder();
        }

        return ErrorCode.Success;
    }

    @Override
    public ErrorCode lifeStart() {
        if (start == config.getStart()) {
            return ErrorCode.Success;
        }
        try {
            this.start = config.getStart();
            configEncoderDecoder();
            configCommunication();
            configInboundStrategy();
            configOutboundStrategy();
        }catch (Exception e) {
            System.out.println("服务启动失败");
            e.printStackTrace();
            return ErrorCode.ServiceStartErr;
        }

        return ErrorCode.Success;
    }

    @Override
    public ErrorCode lifeStop() {
        try{
            if (this.outboundStrategy != null){
                this.outboundStrategy.lifeStop();
            }
            if (this.inboundStrategy != null) {
                this.inboundStrategy.lifeStop();
            }

            for (Communicate communicate : communications) {
                communicate.lifeStop();
            }
            this.communications = new ArrayList<>();
            this.encoderDecoder = null;
            this.start = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ErrorCode.Success;
    }

    public ErrorCode onReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        if (this.inboundStrategy != null) {
            return  this.inboundStrategy.onReceiveMessage(requestMessage,runtimeMessage);
        }

        return ErrorCode.Success;
    }

    public ErrorCode onInboundProcessOver(RequestMessage requestMessage, RuntimeMessage runtimeMessage){
        if (this.outboundStrategy != null) {
            this.outboundStrategy.doReceiveMessage(requestMessage,runtimeMessage);
        }
        return ErrorCode.Success;
    }
    public ErrorCode onOutboundProcessOver(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {

        for(Map.Entry<String,Pair<Long,Long>> entry :runtimeMessage.getTimestramp().entrySet()) {
            Pair<Long,Long> value = entry.getValue();
            System.out.println(String.format("run time:%s %d %d %d",entry.getKey(),value.getKey(),value.getValue(),value.getValue()-value.getKey()));
        }


        return ErrorCode.Success;}
}
