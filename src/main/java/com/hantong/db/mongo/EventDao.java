package com.hantong.db.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hantong.code.ErrorCode;
import com.hantong.db.IEventPersist;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.util.Json;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

public class EventDao implements IEventPersist {

    @JsonIgnore
    private MongoTemplate mongoTemplate;

    public String getCollection() {
        return collection;
    }

    private String collection;
    public EventDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.collection = MongoPersist.COLLECTION_EVENT;
    }

    @Override
    public ErrorCode addRequestMessage(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {


        String writeJson = Json.getInstance().toString(requestMessage);
        Document document = Document.parse(writeJson);

        //mongoTemplate.save(document,this.collection);
        mongoTemplate.save(requestMessage,this.collection);

        return ErrorCode.Success;
    }
}
