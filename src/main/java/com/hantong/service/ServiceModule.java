package com.hantong.service;

import com.hantong.application.ApplicationContext;
import com.hantong.code.ErrorCode;
import com.hantong.codec.EncoderDecoder;
import com.hantong.communication.Communicate;
import com.hantong.inbound.processor.InboundProcessor;
import com.hantong.inbound.strategy.InboundStrategy;
import com.hantong.model.CommunicationConfig;
import com.hantong.model.ServerConfig;
import com.hantong.model.StrategyConfig;
import com.hantong.model.StrategyName;
import com.hantong.outbound.processor.OutboundProcessor;
import com.hantong.outbound.strategy.OutboundStrategy;
import com.hantong.result.Result;
import com.hantong.util.Json;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceModule {

    public Result addDefaultService() {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setId("Service_001");
        serverConfig.setName("Service_name_001");
        serverConfig.setStart(Boolean.TRUE);

        serverConfig.setCodec(EncoderDecoder.Codec_StandardEncoderDeCoder);

        List<CommunicationConfig> communicationConfigs = new ArrayList<>();
        CommunicationConfig cf = new CommunicationConfig();
        cf.setName(CommunicationConfig.CommunicationName.Socket);
        CommunicationConfig.Socket socketCfg = new CommunicationConfig.Socket();
        socketCfg.setPort(5555);
        cf.setSocketCfg(socketCfg);
        communicationConfigs.add(cf);
        serverConfig.setCommunicationConfigs(communicationConfigs);

        StrategyConfig inboundStrategy = new StrategyConfig();
        inboundStrategy.setName(StrategyName.Strategy_Queue);
        inboundStrategy.setQueueSize(10);
        inboundStrategy.setMaxPoolSize(10);
        inboundStrategy.setCorePoolSize(2);
        List<String> inProcessor = new ArrayList<>();
        inProcessor.add(InboundProcessor.InboundProcessor_DbPersis);
        inProcessor.add(InboundProcessor.InboundProcessor_Default);
        inboundStrategy.setProcessor(inProcessor);
        serverConfig.setInboundStrategy(inboundStrategy);

        StrategyConfig outboundStrategy = new StrategyConfig();
        outboundStrategy.setName(StrategyName.Strategy_Queue);
        outboundStrategy.setQueueSize(10);
        outboundStrategy.setMaxPoolSize(10);
        outboundStrategy.setCorePoolSize(2);
        List<String> outProcessor = new ArrayList<>();
        outProcessor.add(OutboundProcessor.OutboundProcessor_Default);
        outProcessor.add(OutboundProcessor.OutboundProcessor_SourceReply);
        outboundStrategy.setProcessor(outProcessor);
        serverConfig.setOutboundStrategy(outboundStrategy);


        ErrorCode result =  ApplicationContext.getServiceManager().addService(serverConfig);

        return new Result(result);
    }

    public Result addService(ServerConfig serverParam) {
        return new Result(ApplicationContext.getServiceManager().addService(serverParam));
    }

    public Result getAllService() {
        return Result.from("services",ApplicationContext.getServiceManager().getAllService());
    }

    public Result startService(String serviceId) {
        return new Result(ApplicationContext.getServiceManager().startService(serviceId));
    }

    public Result stopService(String serviceId) {
        return new Result(ApplicationContext.getServiceManager().stopService(serviceId));
    }

    public Result delService(String serviceId) {
        return new Result(ApplicationContext.getServiceManager().delService(serviceId));
    }

    public String getServiceCfgField() {
        Result result = new Result();
        result.put("service",ServiceManager.getConfigField());
        result.put("codec",EncoderDecoder.getConfigField());
        result.put("communicate",Communicate.getConfigField());
        result.put("inboundStrategy", InboundStrategy.getConfigField());
        result.put("outboundStrategy", OutboundStrategy.getConfigField());
        result.put("inboundProcessor", InboundProcessor.getConfigField());
        result.put("outboundProcessor", OutboundProcessor.getConfigField());

        return Json.getInstance().toString(result);
    }

    public Result monitorService(String serviceId) {
        Service service = ApplicationContext.getServiceManager().getService(serviceId);
        if (null == service) {
            return new Result(ErrorCode.ServiceNotExist);
        }

        Result result = new Result();
        result.put("monitor",service.getMonitorData());
        return result;
    }
}
