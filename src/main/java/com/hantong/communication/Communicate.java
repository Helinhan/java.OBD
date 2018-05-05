package com.hantong.communication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hantong.interfaces.ICodec;
import com.hantong.interfaces.ICommunication;
import com.hantong.interfaces.ILifecycle;
import com.hantong.model.CommunicationConfig;
import com.hantong.model.ServiceConfigField;
import com.hantong.util.Json;

import java.util.List;

public abstract class Communicate implements ICommunication,ILifecycle {

    public static final String Communicate_Socket = CommunicationConfig.CommunicationName.Socket.toString();
    public Communicate(ICodec code) {
        this.encoderDecoder = code;
    }


    protected ICodec encoderDecoder;

    public static List<ServiceConfigField> getConfigField() {
        String config = "[{" +
                "\"name\":\"" + Communicate_Socket +"\"," +
                "\"title\":\"标准Socket\"," +
                "\"description\":\"监听一个Socket端口，跟设备进行对接\"," +
                "\"param\":[" +
                "{\"name\":\"ip\",\"title\":\"要绑定的IP\",\"description\":\"要绑定的服务器IP\",\"range\":\"0.0.0.0表示所有网卡\"}," +
                "{\"name\":\"port\",\"title\":\"要绑定的端口\",\"description\":\"要绑定的服务器端口\",\"range\":\"1024~65535\"}," +
                "{\"name\":\"type\",\"title\":\"Socket类型\",\"description\":\"Socket的类型，TCP或UPD\",\"range\":\"TCP,UDP\"}]" +
                "}]";

        try {
            return Json.getInstance().getObjectMapper().readValue(config, new TypeReference<List<ServiceConfigField>>() {
            });
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
