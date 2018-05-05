package com.hantong.model;

public class CommunicationConfig {
    public static enum CommunicationName {
        Socket,Mqtt,Coap;
    }

    public static class Socket {
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

    public Socket getSocketCfg() {
        return socketCfg;
    }

    public void setSocketCfg(Socket socketCfg) {
        this.socketCfg = socketCfg;
    }

    private CommunicationName name;
    private Socket socketCfg;
}
