package com.fengshang.im.mapper;

import com.fengshang.im.entity.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserSessionDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveUserSession(UserSession userSession) {
        this.mongoTemplate.save(userSession, Integer.toString(userSession.getUserId1()) + "_usersession");//存到对应用户的表中
    }

    public void deleteUserSessionByToUserId(Integer userId1,Integer userId2) {
        Query query = Query.query(Criteria.where("userId2").is(userId2));
        this.mongoTemplate.remove(query, UserSession.class,Integer.toString(userId1) + "_usersession");
    }
    public List<UserSession> findUserSessionByUserId(Integer userId) {
        Query query = Query.query(Criteria.where("userId1").is(userId));
        return this.mongoTemplate.find(query, UserSession.class,Integer.toString(userId) + "_usersession");

    }

    public List<UserSession> findUserSessionByUserIdandToUserId(Integer userId,Integer toUserId) {
        Query query = Query.query(Criteria.where("userId1").is(userId).and("userId2").is(toUserId));
        return this.mongoTemplate.find(query, UserSession.class,Integer.toString(userId) + "_usersession");

    }


}