package com.hantong.inbound.processor;

import com.hantong.code.ErrorCode;
import com.hantong.db.DatabaseManager;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component("DbPersisProcessor")
public class DbPersisProcessor extends InboundProcessor {
    @Autowired
    DatabaseManager databaseManager;

    public DbPersisProcessor() {

    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public ErrorCode receiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        Long begin = System.currentTimeMillis();
        ErrorCode result = databaseManager.getDbInstance().addRequestMessage(requestMessage, runtimeMessage);
        Long end = System.currentTimeMillis();
        runtimeMessage.addTimestramp(this.getClass().getSimpleName(),begin,end);
        return result;
    }

    @Override
    public Map<String, Map<String, String>> getMonitorData() {
        Map<String, Map<String, String>> monitor = new LinkedHashMap<>();
        Map<String, String> thisMonitor = new LinkedHashMap<>();
        thisMonitor.put("dbName","Mongo");
        monitor.put("DbPersisProcessor",thisMonitor);

        return monitor;
    }
}
