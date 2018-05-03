package com.hantong.db;

import com.hantong.db.mongo.MongoPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DatabaseManager {
    @Autowired
    MongoPersist mongo;

    public DatabaseManager(){

    }

    private String type = "mongo";

    public DatabasePersist getDb() {
        if (type.equals("mongo")) {
            return mongo;
        }

        return null;
    }
}
