package com.hantong.inbound.strategy;

import com.hantong.code.ErrorCode;
import com.hantong.inbound.chain.InboundProcessorChain;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.model.StrategyConfig;
import com.hantong.service.Service;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class QueueInboundStrategy extends InboundStrategy {
    private BlockingQueue<BlockingData> queue;
    private ThreadPoolTaskExecutor taskExecutor;
    private Thread threadQueueWait;

    @Override
    public ErrorCode lifeStart() {

        super.lifeStart();

        this.taskExecutor.initialize();
        this.threadQueueWait.start();
        return ErrorCode.Success;
    }

    @Override
    public ErrorCode lifeStop() {
        super.lifeStop();

        if (threadQueueWait != null) threadQueueWait.interrupt();
        threadQueueWait = null;

        if (taskExecutor != null ) taskExecutor.destroy();
        taskExecutor = null;

        return ErrorCode.Success;
    }

    public QueueInboundStrategy(Service s,StrategyConfig config) {
        super(s,config);
        queue = new LinkedBlockingDeque<>();
        this.taskExecutor = new ThreadPoolTaskExecutor();
        this.taskExecutor.setCorePoolSize(config.getCorePoolSize());
        this.taskExecutor.setMaxPoolSize(config.getMaxPoolSize());
        this.taskExecutor.setQueueCapacity(config.getQueueSize());

        QueueProcessor queueProcessor = new QueueProcessor();
        this.threadQueueWait = new Thread(queueProcessor);
    }

    public class BlockingData {
        public RequestMessage requestMessage;
        public RuntimeMessage runtimeMessage;
    }

    private BlockingData getBlockingData(RequestMessage req, RuntimeMessage run) {
        BlockingData data = new BlockingData();
        data.requestMessage = req;
        data.runtimeMessage = run;

        return data;
    }

    private ErrorCode addQueue(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        Long begin = System.currentTimeMillis();
        queue.add(this.getBlockingData(requestMessage,runtimeMessage));
        Long end = System.currentTimeMillis();

        runtimeMessage.addTimestramp(this.getClass().getSimpleName(),begin,end);

        return ErrorCode.Success;
    }

    @Override
    public ErrorCode onReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        return this.addQueue(requestMessage,runtimeMessage);
    }

    /*
    这个线程才是每个流程的处理过程
     */
    public class ChainProcessor implements Runnable {
        private BlockingData blockingData;
        public ChainProcessor(BlockingData data) {
            this.blockingData = data;
        }
        @Override
        public void run() {
            Long begin = System.currentTimeMillis();
            inboundProcessorChain.onReceiveMessage(this.blockingData.requestMessage,this.blockingData.runtimeMessage);
            Long end = System.currentTimeMillis();

            this.blockingData.runtimeMessage.addTimestramp(this.getClass().getSimpleName(),begin,end);

            processOver(this.blockingData.requestMessage,this.blockingData.runtimeMessage);
        }
    }

    /*
    这个线程是为了不让主线等待
     */
    public class QueueProcessor implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    BlockingData blockingData = queue.take();
                    taskExecutor.execute(new ChainProcessor(blockingData));

                } catch (Exception e) {

                }
            }
        }
    }

    @Override
    public Map<String, Map<String, String>> getMonitorData() {
        Map<String, Map<String, String>> monitor = new LinkedHashMap<>();
        Map<String, String> thisMonitor = new LinkedHashMap<>();
        thisMonitor.put("remainingCapacity",String.valueOf(queue.remainingCapacity()));
        monitor.put("QueueInboundStrategy",thisMonitor);
        monitor.putAll(inboundProcessorChain.getMonitorData());

        return monitor;
    }
}
