package com.hantong.inbound.processor;

import com.hantong.code.ErrorCode;
import com.hantong.db.DatabaseManager;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("DbPersisProcessor")
public class DbPersisProcessor extends InboundProcessor {

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Autowired
    DatabaseManager databaseManager;

    public DbPersisProcessor() {

    }

    @Override
    public ErrorCode receiveMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        Long begin = System.currentTimeMillis();
        ErrorCode result = databaseManager.getDbInstance().addRequestMessage(requestMessage, runtimeMessage);
        Long end = System.currentTimeMillis();
        runtimeMessage.addTimestramp(this.getClass().getSimpleName(),begin,end);
        return result;
    }
}
