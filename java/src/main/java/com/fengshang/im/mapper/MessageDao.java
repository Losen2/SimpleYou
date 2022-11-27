package com.fengshang.im.mapper;

import com.fengshang.im.entity.Message;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.VariableOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class MessageDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveMessageByUser(Message message) {
        this.mongoTemplate.save(message, Integer.toString(message.getUserId()) + "_message");//存到对应用户的表中
    }

    public void saveMessageByToUser(Message message) {
        this.mongoTemplate.save(message, Integer.toString(message.getToUserId()) + "_message");//存到对应接收用户的表中
    }

    public List<Message> getMessage(Integer userId,Integer toUserId) {
        Criteria criteria1 = where("userId").is(userId).and("toUserId").is(toUserId);
        Criteria criteria2 = where("userId").is(toUserId).and("toUserId").is(userId);
        Criteria criteria = new Criteria().orOperator(criteria1, criteria2);
        Query query = Query.query(criteria);
        return this.mongoTemplate.find(query, Message.class,Integer.toString(userId) + "_message");
    }

    public List<Message> getRecentMessage(Integer userId,long mongoId){//时间太久就拉3天 时间不久就拉完
        long nowDate = new Date().getTime();
        Criteria criteria1 = null;

        if(nowDate-mongoId>24*3600*7*1000){//7天以上
            criteria1 = where("behindTime").gt(nowDate-24*3600*3*1000);//三天之内的消息
        }
        else {//否则按需拉取
            criteria1 = where("behindTime").gt(mongoId);
        }

        Criteria criteria2 = where("readCount").is(0).and("toUserId").is(userId);//同时拉取未读消息
        Criteria criteria = new Criteria().orOperator(criteria1, criteria2);

        Query query = Query.query(criteria);
        return this.mongoTemplate.find(query, Message.class,Integer.toString(userId) + "_message");
    }

    public void clearUnreadCount(Integer userId, Integer toUserId) {
        Criteria criteria = where("userId").is(toUserId).and("readCount").is(0);
        Query query = Query.query(criteria);
        Update update = new Update();
        update.set("readCount",1);
        mongoTemplate.updateMulti(query, update, Integer.toString(userId) + "_message");
    }






}
