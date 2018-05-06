package com.hantong.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hantong.code.ErrorCode;
import com.hantong.model.ServerConfig;
import com.hantong.model.ServiceConfigField;
import com.hantong.util.Json;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
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

    public Map<String,ServerConfig> getAllService() {
        Map<String,ServerConfig> result = new HashMap<>();
        for (Map.Entry<String,Service> serviceEntry : this.serviceMap.entrySet()) {
            result.put(serviceEntry.getKey(),serviceEntry.getValue().getConfig());
        }
        return result;
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

    public static List<ServiceConfigField> getConfigField() {
        String config = "[" +
                "{" +
                "\"param\":[" +
                "{\"name\":\"name\",\"title\":\"服务名\",\"description\":\"服务的名称\",\"range\":\"按需配置\"}," +
                "{\"name\":\"id\",\"title\":\"服务ID\",\"description\":\"一个服务的唯一标识\",\"range\":\"按指定方式生成\"}," +
                "{\"name\":\"start\",\"title\":\"是否启动\",\"description\":\"配置完是否直接启动\",\"range\":\"true,false\"}]" +
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
