package com.hantong.outbound.strategy;

import com.hantong.code.ErrorCode;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.model.StrategyConfig;
import com.hantong.service.Service;
import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class QueueOutboundStrategy extends OutboundStrategy {
    private BlockingQueue<BlockingData> queue;
    private ThreadPoolTaskExecutor taskExecutor;
    private Thread threadQueueWait;

    private Logger LOGGER = Logger.getLogger(QueueOutboundStrategy.class);

    public QueueOutboundStrategy(Service s,StrategyConfig config){
        super(s,config);
        queue = new LinkedBlockingDeque<>();
        this.taskExecutor = new ThreadPoolTaskExecutor();
        this.taskExecutor.setCorePoolSize(config.getCorePoolSize());
        this.taskExecutor.setMaxPoolSize(config.getMaxPoolSize());
        this.taskExecutor.setQueueCapacity(config.getQueueSize());

        QueueProcessor queueProcessor = new QueueProcessor();
        this.threadQueueWait = new Thread(queueProcessor);
    }

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

        if (null != threadQueueWait) this.threadQueueWait.interrupt();
        this.threadQueueWait = null;

        if (null != taskExecutor) this.taskExecutor.destroy();
        this.taskExecutor = null;

        return ErrorCode.Success;
    }

    @Override
    public ErrorCode doReceiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        LOGGER.info("doReceiveMessage");
        this.addQueue(requestMessage,runtimeMessage);
        return ErrorCode.Success;
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
            try {
                outboundProcessorChain.doReceiveMessage(this.blockingData.requestMessage, this.blockingData.runtimeMessage);
            }catch (Exception e) {
                if (null != e) e.printStackTrace();
            }
            processOver(this.blockingData.requestMessage,this.blockingData.runtimeMessage);
        }
    }

    /*
    这个线程是为了不让主线等待
     */
    public class QueueProcessor implements Runnable {
        @Override
        public void run() {
            while(true) {
                try {
                    BlockingData blockingData = queue.take();
                    taskExecutor.execute(new ChainProcessor(blockingData));
                } catch (Exception e) { }
            }
        }
    }
}
