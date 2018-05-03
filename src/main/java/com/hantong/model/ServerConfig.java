package com.hantong.model;

public class ServerConfig {
    private String name;
    private String id;
    private StrategyConfig inboundStrategy;
    private StrategyConfig outboundStrategy;
    private Boolean start;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getStart() {
        return start;
    }

    public void setStart(Boolean start) {
        this.start = start;
    }

    public StrategyConfig getInboundStrategy() {
        return inboundStrategy;
    }

    public void setInboundStrategy(StrategyConfig inboundStrategy) {
        this.inboundStrategy = inboundStrategy;
    }

    public StrategyConfig getOutboundStrategy() {
        return outboundStrategy;
    }

    public void setOutboundStrategy(StrategyConfig outboundStrategy) {
        this.outboundStrategy = outboundStrategy;
    }

}
