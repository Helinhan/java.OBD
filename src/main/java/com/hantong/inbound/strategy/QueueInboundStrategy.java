package com.hantong.inbound.strategy;

import com.hantong.code.ErrorCode;
import com.hantong.inbound.chain.InboundProcessorChain;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.service.Service;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.BlockingQueue;

public class QueueInboundStrategy extends InboundStrategy {
    private BlockingQueue<BlockingData> queue;
    private InboundProcessorChain inboundProcessorChain;
    private ThreadPoolTaskExecutor taskExecutor;
    private Thread threadQueueWait;

    @Override
    public ErrorCode lifeStart() {
        return null;
    }

    @Override
    public ErrorCode lifeStop() {
        threadQueueWait.interrupt();
        threadQueueWait = null;

        taskExecutor.destroy();
        taskExecutor = null;

        return ErrorCode.Success;
    }

    public QueueInboundStrategy(Service s) {
        super(s);
        this.taskExecutor = new ThreadPoolTaskExecutor();
        this.taskExecutor.setCorePoolSize(3);
        this.taskExecutor.setMaxPoolSize(10);
        this.taskExecutor.setQueueCapacity(10);
        this.taskExecutor.initialize();

        QueueProcessor queueProcessor = new QueueProcessor();
        this.threadQueueWait = new Thread(queueProcessor);
        this.threadQueueWait.start();
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
        queue.add(this.getBlockingData(requestMessage,runtimeMessage));
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
            inboundProcessorChain.onReceiveMessage(this.blockingData.requestMessage,this.blockingData.runtimeMessage);
            processOver();
        }
    }

    /*
    这个线程是为了不让主线等待
     */
    public class QueueProcessor implements Runnable {
        @Override
        public void run() {
            try {
                BlockingData blockingData = queue.take();
                taskExecutor.execute(new ChainProcessor(blockingData));

            } catch (Exception e) {

            }
        }
    }
}
