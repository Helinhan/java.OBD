package com.hantong.model;


import java.util.List;

public class StrategyConfig {
    private StrategyType type;
    private Integer queueSize;
    private Integer maxPoolSize;
    private Integer corePoolSize;
    private List<String> processor;

    public List<String> getProcessor() {
        return processor;
    }

    public void setProcessor(List<String> processor) {
        this.processor = processor;
    }

    public StrategyType getType() {
        return type;
    }

    public void setType(StrategyType type) {
        this.type = type;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }
}
