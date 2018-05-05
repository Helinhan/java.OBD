package com.hantong.inbound.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hantong.interfaces.IInboundProcesser;
import com.hantong.model.ServiceConfigField;
import com.hantong.util.Json;

import java.util.List;

public abstract class InboundProcessor implements IInboundProcesser {
    public String getName() {
        return name;
    }

    private String name = this.getClass().getName();

    public static final String InboundProcessor_Default = "DefaultInboundProcessor";
    public static final String InboundProcessor_DbPersis = "DbPersisProcessor";

    public static List<ServiceConfigField> getConfigField() {
        String config = "[" +
                "{" +
                "\"name\":\""+InboundProcessor_Default+"\"," +
                "\"title\":\"默认执行策略\"," +
                "\"description\":\"对于每个入栈数据进行默认处理（只记录日志等）\"" +
                "}," +
                "{" +
                "\"name\":\""+InboundProcessor_DbPersis+"\"," +
                "\"title\":\"串行执行策略\"," +
                "\"description\":\"数据库持久化\"" +
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
}
