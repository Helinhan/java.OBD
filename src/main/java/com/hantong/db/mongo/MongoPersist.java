package com.hantong.db.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hantong.code.ErrorCode;
import com.hantong.db.DatabasePersist;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("MongoPersist")
public class MongoPersist extends DatabasePersist {

    public final static String COLLECTION_EVENT = "event";
    public final static String COLLECTION_USER = "user";

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

    @Override
    public User findUser(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return this.mongoTemplate.findOne(query,User.class,COLLECTION_USER);
    }

    @Override
    public ErrorCode addUser(User user) {
        this.mongoTemplate.save(user,COLLECTION_USER);
        return ErrorCode.Success;
    }

    @Override
    public List<User> findUserByRole(String role) {
        Query query = new Query();
        query.addCriteria(Criteria.where("role").in(role));
        return this.mongoTemplate.find(query,User.class,COLLECTION_USER);
    }
}
