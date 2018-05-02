package com.hantong.service.collection;

import com.hantong.service.persistence.PersistenceBlocking;
import com.hantong.service.persistence.PersistenceData;

import java.util.List;
import java.util.Random;

public class CollectionTask implements Runnable{

    PersistenceBlocking persistenceBlocking;

    private List<String> collList;
    public CollectionTask(List<String> coll,PersistenceBlocking persistenceBlock) {
        this.collList = coll;
        this.persistenceBlocking = persistenceBlock;
    }

    @Override
    public void run() {
        Random random = new Random();
        Long begin = System.currentTimeMillis();
        try {
            int sleep = random.nextInt(500);
            Thread.sleep(sleep);
            PersistenceData data = new PersistenceData();
            data.setType(0);
            data.setValue(String.valueOf(sleep));

            persistenceBlocking.addPersistenceData(data);
        }catch (Exception e) {}


        Long end = System.currentTimeMillis();
        //System.out.println(String.format("thread:%d time:%d",Thread.currentThread().getId(),end - begin));
    }
}
