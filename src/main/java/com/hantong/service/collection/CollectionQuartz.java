package com.hantong.service.collection;

import com.hantong.service.persistence.PersistenceBlocking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@EnableScheduling
public class CollectionQuartz {

    @Autowired
    private CollectionTaskExecutor collectionTaskExecutor;

    @Autowired
    private PersistenceBlocking persistenceBlocking;

    public CollectionQuartz(){
        System.out.println("iit......");
    }

    @Scheduled(cron="* * *  * * ?")
    public void collecionCron(){
        //System.out.println("collecionCron start!!");
        List<String> tasks = new LinkedList<>();
        tasks.add("a");

        CollectionTask task = new CollectionTask(tasks,persistenceBlocking);
        this.collectionTaskExecutor.getTaskExecutor().execute(task);
        this.collectionTaskExecutor.getTaskExecutor().execute(task);
        this.collectionTaskExecutor.getTaskExecutor().execute(task);
        this.collectionTaskExecutor.getTaskExecutor().execute(task);
        this.collectionTaskExecutor.getTaskExecutor().execute(task);
    }
}
