package com.hantong.outbound.processor;


import com.fasterxml.jackson.core.type.TypeReference;
import com.hantong.code.ErrorCode;
import com.hantong.interfaces.IMonitor;
import com.hantong.interfaces.IOutboundProcesser;
import com.hantong.model.ServiceConfigField;
import com.hantong.model.StrategyConfig;
import com.hantong.outbound.chain.OutboundProcessorChain;
import com.hantong.util.Json;

import java.util.List;

public abstract class OutboundProcessor implements IOutboundProcesser,IMonitor {
    public String getName() {
        return name;
    }

    private String name = this.getClass().getName();

    public static final String OutboundProcessor_Default = "DefaultOutboundProcessor";
    public static final String OutboundProcessor_SourceReply = "SourceReplyOutboundProcessor";

    public static List<ServiceConfigField> getConfigField() {
        String config = "[" +
                "{" +
                "\"name\":\""+OutboundProcessor_Default+"\"," +
                "\"title\":\"默认执行策略\"," +
                "\"description\":\"对于每个入栈数据进行默认处理（只记录日志等）\"" +
                "}," +
                "{" +
                "\"name\":\""+OutboundProcessor_SourceReply+"\"," +
                "\"title\":\"源路径回复\"," +
                "\"description\":\"从收到的通信节点回告消息\"" +
                "}"+
                "]";

        try {
            return Json.getInstance().getObjectMapper().readValue(config, new TypeReference<List<ServiceConfigField>>() {
            });
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ErrorCode build(OutboundProcessorChain outboundProcessorChain,StrategyConfig config){
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
}
