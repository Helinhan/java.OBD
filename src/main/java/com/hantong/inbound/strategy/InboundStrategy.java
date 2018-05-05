package com.hantong.inbound.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hantong.HantongApplication;
import com.hantong.code.ErrorCode;
import com.hantong.inbound.chain.InboundProcessorChain;
import com.hantong.inbound.processor.DbPersisProcessor;
import com.hantong.inbound.processor.DefaultInboundProcessor;
import com.hantong.inbound.processor.InboundProcessor;
import com.hantong.interfaces.IInbound;
import com.hantong.interfaces.ILifecycle;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.model.ServiceConfigField;
import com.hantong.model.StrategyConfig;
import com.hantong.service.Service;
import com.hantong.util.Json;

import java.util.List;

import static com.hantong.model.StrategyName.Strategy_Block;
import static com.hantong.model.StrategyName.Strategy_Default;
import static com.hantong.model.StrategyName.Strategy_Queue;

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
            if (processor.equals(InboundProcessor.InboundProcessor_DbPersis)) {
                DbPersisProcessor processor1 = (DbPersisProcessor)HantongApplication.getApplicationContext().getBean("DbPersisProcessor");
                inboundProcessorChain.addProcessor(processor1);
            } else if (processor.equals(InboundProcessor.InboundProcessor_Default)) {
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

    public static List<ServiceConfigField> getConfigField() {
        String config = "[" +
                "{" +
                "\"name\":\""+Strategy_Default.toString()+"\"," +
                "\"title\":\"默认执行策略\"," +
                "\"description\":\"对于每个入栈数据进行默认处理（只记录日志等）\"" +
                "}," +
                "{" +
                "\"name\":\""+Strategy_Block.toString()+"\"," +
                "\"title\":\"串行执行策略\"," +
                "\"description\":\"对于每个入栈数据进行串行执行\"" +
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
