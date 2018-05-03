package com.hantong.db.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hantong.db.DatabasePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component("MongoPersist")
public class MongoPersist extends DatabasePersist {

    public final static String COLLECTION_EVENT = "event";

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.setEvent(new EventDao(mongoTemplate));
    }

    @JsonIgnore
    private MongoTemplate mongoTemplate;

    public MongoPersist() {

    }
}
