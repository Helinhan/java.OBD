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

    public List<Communicate> getCommunications() {
        return communications;
    }

    public void setCommunications(List<Communicate> communications) {
        this.communications = communications;
    }

    public EncoderDecoder getEncoderDecoder() {
        return encoderDecoder;
    }

    public void setEncoderDecoder(EncoderDecoder encoderDecoder) {
        this.encoderDecoder = encoderDecoder;
    }

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
        this.inboundStrategy = InboundStrategy.build(this,config);

        return ErrorCode.Success;
    }

    private ErrorCode configOutboundStrategy() throws ErrorCodeException{
        this.outboundStrategy = OutboundStrategy.build(this,config);
        return ErrorCode.Success;
    }

    private ErrorCode configCommunication() throws ErrorCodeException{
        return Communicate.build(this,config);
    }

    private ErrorCode configEncoderDecoder() throws ErrorCodeException{
        return EncoderDecoder.build(this,config);
    }

    @Override
    public ErrorCode lifeStart() {
        if (start) {
            return ErrorCode.Success;
        }
        try {
            configInboundStrategy();
            configOutboundStrategy();
            configEncoderDecoder();
            configCommunication();

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
            for (Communicate communicate : communications) {
                communicate.lifeStop();
            }
            this.communications = new ArrayList<>();

            if (this.outboundStrategy != null){
                this.outboundStrategy.lifeStop();
                this.outboundStrategy = null;
            }
            if (this.inboundStrategy != null) {
                this.inboundStrategy.lifeStop();
                this.outboundStrategy = null;
            }

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
