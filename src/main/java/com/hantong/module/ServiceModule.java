package com.hantong.module;

import com.hantong.application.ApplicationContext;
import com.hantong.code.ErrorCode;
import com.hantong.model.CommunicationConfig;
import com.hantong.model.ServerConfig;
import com.hantong.model.StrategyConfig;
import com.hantong.model.StrategyType;
import com.hantong.result.Result;
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

        serverConfig.setCodec("StandardEncoderDeCoder");

        List<CommunicationConfig> communicationConfigs = new ArrayList<>();
        CommunicationConfig cf = new CommunicationConfig();
        cf.setType(CommunicationConfig.CommunicationType.Socket);
        CommunicationConfig.Socket socketCfg = new CommunicationConfig.Socket();
        socketCfg.setPort(5555);
        cf.setSocketCfg(socketCfg);
        communicationConfigs.add(cf);
        serverConfig.setCommunicationConfigs(communicationConfigs);

        StrategyConfig inboundStrategy = new StrategyConfig();
        inboundStrategy.setType(StrategyType.Strategy_Queue);
        inboundStrategy.setQueueSize(10);
        inboundStrategy.setMaxPoolSize(10);
        inboundStrategy.setCorePoolSize(2);
        List<String> inProcessor = new ArrayList<>();
        inProcessor.add("DbPersisProcessor");
        inProcessor.add("DefaultProcessor");
        inboundStrategy.setProcessor(inProcessor);
        serverConfig.setInboundStrategy(inboundStrategy);

        StrategyConfig outboundStrategy = new StrategyConfig();
        outboundStrategy.setType(StrategyType.Strategy_Queue);
        outboundStrategy.setQueueSize(10);
        outboundStrategy.setMaxPoolSize(10);
        outboundStrategy.setCorePoolSize(2);
        List<String> outProcessor = new ArrayList<>();
        outProcessor.add("DefaultProcessor");
        outProcessor.add("SourceReplyOutboundProcessor");
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
}
