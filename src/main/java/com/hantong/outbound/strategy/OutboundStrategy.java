package com.hantong.outbound.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hantong.code.ErrorCode;
import com.hantong.interfaces.ILifecycle;
import com.hantong.interfaces.IOutbound;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.model.ServiceConfigField;
import com.hantong.model.StrategyConfig;
import com.hantong.outbound.chain.OutboundProcessorChain;
import com.hantong.outbound.processor.DefaultOutboundProcessor;
import com.hantong.outbound.processor.OutboundProcessor;
import com.hantong.outbound.processor.SourceReplyOutboundProcessor;
import com.hantong.service.Service;
import com.hantong.util.Json;

import java.util.List;

import static com.hantong.model.StrategyName.Strategy_Default;
import static com.hantong.model.StrategyName.Strategy_Queue;

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
            if (processor.equals(OutboundProcessor.OutboundProcessor_Default)) {
                DefaultOutboundProcessor processor1 = new DefaultOutboundProcessor();
                outboundProcessorChain.addProcessor(processor1);
            } else if (processor.equals(OutboundProcessor.OutboundProcessor_SourceReply)) {
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


    public static List<ServiceConfigField> getConfigField() {
        String config = "[" +
                "{" +
                "\"name\":\""+Strategy_Default.toString()+"\"," +
                "\"title\":\"默认执行策略\"," +
                "\"description\":\"对于每个入栈数据进行默认处理（只记录日志等）\"" +
                "}," +
                "{" +
                "\"name\":\""+Strategy_Queue.toString()+"\"," +
                "\"title\":\"并发执行策略\"," +
                "\"description\":\"对于每个入栈数据进行入队列，而后并发执行\"," +
                "\"param\":[" +
                "{\"name\":\"queueSize\",\"title\":\"队列深度\",\"description\":\"缓存数据的队列尝试\",\"range\":\"按需配置，大于等于1\"}," +
                "{\"name\":\"maxPoolSize\",\"title\":\"最多线程数\",\"description\":\"并发最大执行的线程数\",\"range\":\"按需配置，大于等于1\"}," +
                "{\"name\":\"corePoolSize\",\"title\":\"常用并发数\",\"description\":\"正常情况下的并发数\",\"range\":\"按需配置，大于等于1\"}]" +
                "}" +
                "]";

        try {
            return Json.getInstance().getObjectMapper().readValue(config, new TypeReference<List<ServiceConfigField>>() {
            });
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
