package com.hantong.communication;

public class CommunicationConfig {
    public static enum CommunicationName {
        Socket,Mqtt,Coap;
    }

    public static class Param {
        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private Integer port;
        private String  ip = "0.0.0.0";
        private String  type = "TCP";
    }

    public CommunicationName getName() {
        return name;
    }

    public void setName(CommunicationName name) {
        this.name = name;
    }

    public Param getParam() {
        return param;
    }

    public void setParam(Param param) {
        this.param = param;
    }

    private CommunicationName name;
    private Param param;
}
