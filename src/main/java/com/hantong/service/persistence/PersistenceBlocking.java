package com.hantong.service.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class PersistenceBlocking {

    private BlockingQueue<PersistenceData> queue;
    private Thread thread;

    @Autowired
    public PersistenceBlocking(Environment env) {
        int size = Integer.valueOf(env.getProperty("collection.persistence.queue.size"));
        this.queue = new ArrayBlockingQueue<PersistenceData>(size);

        PersistenceProcessor processor = new PersistenceProcessor(this.queue);

        this.thread = new Thread(processor);
        this.thread.start();
    }

    public void addPersistenceData(PersistenceData data) {
        this.queue.add(data);
    }

    private class PersistenceProcessor implements Runnable {
        private BlockingQueue<PersistenceData> queue;
        public PersistenceProcessor(BlockingQueue<PersistenceData> que){
            this.queue = que;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    PersistenceData data = queue.take();
                    //System.out.println(String.format("persis:%s",data.getValue()));
                } catch (Exception e) {
                }
            }
        }
    }
}
