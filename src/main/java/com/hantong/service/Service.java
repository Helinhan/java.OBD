package com.hantong.service;

import com.hantong.codec.EncoderDecoder;
import com.hantong.codec.StandardEncoderDeCoder;
import com.hantong.code.ErrorCode;
import com.hantong.communication.Communicate;
import com.hantong.communication.component.SocketCommunicate;
import com.hantong.exception.ErrorCodeException;
import com.hantong.inbound.strategy.BlockInboundStrategy;
import com.hantong.inbound.strategy.DefaultInboundStrategy;
import com.hantong.inbound.strategy.InboundStrategy;
import com.hantong.inbound.strategy.QueueInboundStrategy;
import com.hantong.interfaces.ILifecycle;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.model.CommunicationConfig;
import com.hantong.model.Pair;
import com.hantong.model.ServerConfig;
import com.hantong.outbound.strategy.DefaultOutboundStrategy;
import com.hantong.outbound.strategy.OutboundStrategy;
import com.hantong.outbound.strategy.QueueOutboundStrategy;
import com.hantong.util.Echo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hantong.model.StrategyName.Strategy_Block;
import static com.hantong.model.StrategyName.Strategy_Default;
import static com.hantong.model.StrategyName.Strategy_Queue;

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

    private ErrorCode configInboundStrategy() throws ErrorCodeException{
        if (config.getInboundStrategy().getName() == Strategy_Default)
        {
            this.inboundStrategy = new DefaultInboundStrategy(this,config.getInboundStrategy());
        }
        else if (config.getInboundStrategy().getName() == Strategy_Block) {
            this.inboundStrategy = new BlockInboundStrategy(this,config.getInboundStrategy());
        }
        else if (config.getInboundStrategy().getName() == Strategy_Queue) {
            this.inboundStrategy = new QueueInboundStrategy(this,config.getInboundStrategy());
        }
        else {
            throw new ErrorCodeException(ErrorCode.ServiceStartErr);
        }

        if (ErrorCode.Success != this.inboundStrategy.lifeStart()){
            throw new ErrorCodeException(ErrorCode.ServiceStartErr);
        }

        return ErrorCode.Success;
    }

    private ErrorCode configOutboundStrategy() throws ErrorCodeException{
        if (config.getOutboundStrategy().getName() == Strategy_Default) {
            this.outboundStrategy = new DefaultOutboundStrategy(this,config.getOutboundStrategy());
        } else if (config.getOutboundStrategy().getName() == Strategy_Queue) {
            this.outboundStrategy = new QueueOutboundStrategy(this,config.getOutboundStrategy());
        } else {
            throw new ErrorCodeException(ErrorCode.ServiceStartErr);
        }

        if (ErrorCode.Success != this.outboundStrategy.lifeStart()) {
            throw new ErrorCodeException(ErrorCode.ServiceStartErr);
        }

        return ErrorCode.Success;
    }

    private ErrorCode configCommunication() throws ErrorCodeException{
        for (CommunicationConfig conf:config.getCommunicationConfigs())
        {
            if (conf.getName() == CommunicationConfig.CommunicationName.Socket) {
                Communicate communication = new SocketCommunicate(conf.getSocketCfg(),this.encoderDecoder,this);
                if (ErrorCode.Success != communication.lifeStart()){
                    throw new ErrorCodeException(ErrorCode.ServiceStartErr);
                }
                this.communications.add(communication);
            } else {
                throw new ErrorCodeException(ErrorCode.ServiceStartErr);
            }
        }

        return ErrorCode.Success;
    }

    private ErrorCode configEncoderDecoder() throws ErrorCodeException{
        if (this.config.getCodec().equals(EncoderDecoder.Codec_StandardEncoderDeCoder)) {
            this.encoderDecoder = new StandardEncoderDeCoder();
            return ErrorCode.Success;
        }

        throw new ErrorCodeException(ErrorCode.ServiceStartErr);
    }

    @Override
    public ErrorCode lifeStart() {
        if (start) {
            return ErrorCode.Success;
        }
        try {
            configEncoderDecoder();
            configCommunication();
            configInboundStrategy();
            configOutboundStrategy();
            this.start = true;
        }catch (ErrorCodeException e) {
            System.out.println("服务启动失败:" + e.getErrorCode().getMessage());
            e.printStackTrace();
            this.start = false;
            try {
                this.lifeStop();
            } catch (Exception e1) {}
            return ErrorCode.ServiceStartErr;
        }

        return ErrorCode.Success;
    }

    @Override
    public ErrorCode lifeStop() {
        try{
            if (this.outboundStrategy != null){
                this.outboundStrategy.lifeStop();
                this.outboundStrategy = null;
            }
            if (this.inboundStrategy != null) {
                this.inboundStrategy.lifeStop();
                this.outboundStrategy = null;
            }

            for (Communicate communicate : communications) {
                communicate.lifeStop();
            }
            this.communications = new ArrayList<>();
            this.encoderDecoder = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.start = false;
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

        Echo.green("运行统计\n-----------------------------------");
        for(Map.Entry<String,Pair<Long,Long>> entry :runtimeMessage.getTimestramp().entrySet()) {
            Pair<Long,Long> value = entry.getValue();
            Echo.green(String.format("run time:%s %d %d %d",entry.getKey(),value.getKey(),value.getValue(),value.getValue()-value.getKey()));
        }

        return ErrorCode.Success;
    }
}
