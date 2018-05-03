package com.hantong.service;

import com.hantong.code.ErrorCode;
import com.hantong.model.ServerConfig;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceManager {
    private Map<String,Service> serviceMap;


    public ServiceManager() {
        serviceMap = new HashMap<>();
    }

    public Service getService(String id) {
        return this.serviceMap.getOrDefault(id,null);
    }

    public Map<String,Service> getAllService() {
        return this.serviceMap;
    }

    public ErrorCode addService(ServerConfig serverParam) {
        Service service = new Service(serverParam);
        service.lifeStart();
        this.serviceMap.put(serverParam.getId(),service);
        return ErrorCode.Success;
    }

    public ErrorCode delService(String serverId) {
        this.stopService(serverId);
        this.serviceMap.remove(serverId);
        return ErrorCode.Success;
    }

    public ErrorCode startService(String serverId) {
        return ErrorCode.Success;
    }

    public ErrorCode stopService(String serverId) {
        return ErrorCode.Success;
    }
}
