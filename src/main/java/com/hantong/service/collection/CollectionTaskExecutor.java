package com.hantong.service.collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class CollectionTaskExecutor {

    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;

    public ThreadPoolTaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;

    }

    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    public CollectionTaskExecutor(Environment env) {
        this.queueCapacity = Integer.valueOf(env.getProperty("collection.queue.capacity"));
        this.maxPoolSize = Integer.valueOf(env.getProperty("collection.max.pool.size"));
        this.corePoolSize= Integer.valueOf(env.getProperty("collection.core.pool.size"));

        this.taskExecutor = new ThreadPoolTaskExecutor();
        this.taskExecutor.setCorePoolSize(this.corePoolSize);
        this.taskExecutor.setMaxPoolSize(this.maxPoolSize);
        this.taskExecutor.setQueueCapacity(this.queueCapacity);
        this.taskExecutor.initialize();
    }
}
