package com.hantong.model;

public class CommunicationConfig {
    public static enum  CommunicationType {
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

    public CommunicationType getType() {
        return type;
    }

    public void setType(CommunicationType type) {
        this.type = type;
    }

    public Socket getSocketCfg() {
        return socketCfg;
    }

    public void setSocketCfg(Socket socketCfg) {
        this.socketCfg = socketCfg;
    }

    private CommunicationType type;
    private Socket socketCfg;
}
