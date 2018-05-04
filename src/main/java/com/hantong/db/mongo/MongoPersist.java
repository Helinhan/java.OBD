package com.hantong.db.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hantong.code.ErrorCode;
import com.hantong.db.DatabasePersist;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component("MongoPersist")
public class MongoPersist extends DatabasePersist {

    public final static String COLLECTION_EVENT = "event";

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @JsonIgnore
    private MongoTemplate mongoTemplate;

    public MongoPersist() {
    }

    @Override
    public ErrorCode addRequestMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        mongoTemplate.save(requestMessage,COLLECTION_EVENT);

        return ErrorCode.Success;
    }
}
