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
        if (this.getService(serverParam.getId()) != null) {
            return ErrorCode.Success;
        }

        Service service = new Service(serverParam);
        if (serverParam.getStart()) {
            service.lifeStart();
        } else {
            service.lifeStop();
        }
        this.serviceMap.put(serverParam.getId(),service);
        return ErrorCode.Success;
    }

    public ErrorCode delService(String serverId) {
        this.stopService(serverId);
        this.serviceMap.remove(serverId);
        return ErrorCode.Success;
    }

    public ErrorCode startService(String serverId) {
        Service service = this.getService(serverId);
        if (null != service) {
            return service.lifeStart();
        }
        return ErrorCode.ServiceNotExist;
    }

    public ErrorCode stopService(String serverId) {
        Service service = this.getService(serverId);
        if (null != service) {
            return service.lifeStop();
        }
        return ErrorCode.ServiceNotExist;
    }
}
