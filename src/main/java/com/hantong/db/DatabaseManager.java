package com.hantong.db;

import com.hantong.db.mongo.MongoPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DatabaseManager {
    @Autowired
    MongoPersist mongo;

    public DatabaseManager(){ }


    public DatabasePersist getDbInstance(String dbType) {
        if (dbType.equals("mongo")) {
            return mongo;
        } else {
            return null;
        }
    }

    public DatabasePersist getDbInstance() {
        return this.getDbInstance("mongo");
    }
}
