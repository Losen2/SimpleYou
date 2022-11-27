package com.fengshang.im.controller;


import com.fengshang.im.entity.UserSession;
import com.fengshang.im.mapper.MessageDao;
import com.fengshang.im.mapper.UserSessionDao;
import com.fengshang.im.utils.*;
import com.fengshang.im.mapper.UserMapper;
import com.fengshang.im.entity.Message;
import com.fengshang.im.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import com.fengshang.im.utils.CurPool;

import java.util.Date;

@Component
@ServerEndpoint("/websocket/{userId}")
//此注解相当于设置访问URL
public class WebSocket {



    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private UserSessionDao userSessionDao;

    private Session session;


    @OnOpen
    public void onOpen(Session session,@PathParam(value="userId")Integer userId) {
        this.session = session;
        CurPool.webSockets.put(userId,this);

        System.out.println("【websocket消息】有新的连接 id为"+userId+"，总数为:"+CurPool.webSockets.size());
    }

    @OnClose
    public void onClose() {
        // 断开连接删除用户删除session
        Integer userId = Integer.parseInt(this.session.getRequestParameterMap().get("userId").get(0));

        CurPool.webSockets.remove(userId);
        if (userMapper == null){
            this.userMapper = (UserMapper) SpringContextUtil.getBean("userMapper");
        }
        User user = userMapper.selectByPrimaryKey(userId);
        System.out.println("【websocket消息】连接断开 id为"+userId+"，总数为:"+CurPool.webSockets.size());
    }



    @OnMessage
    public void onMessage(String message) throws Exception {
        if (messageDao == null){
            this.messageDao = (MessageDao) SpringContextUtil.getBean("messageDao");
        }
        Message msg = JsonUtils.jsonToPojo(message, Message.class);
        System.out.println("---------------------------------");
        System.out.println(msg);
        if(msg.getTemp()==1&&msg.getUpdate()==0){
            Date curDate = new Date();
            msg.setTemp(0);
            msg.setId(null);
            msg.setBehindTime(curDate.getTime());//后端的时间

            messageDao.saveMessageByUser(msg);//此时msg1已经带上了id（user的表）
            //对user发送确认消息以更新前端状态（发送中->消息未读） 还没写
            //

            //不管对面在不在 都要存对面的mongo一份 注意状态这里应该是未读 不过先不写
            msg.setId(null);

            messageDao.saveMessageByToUser(msg);
            //对消息发出者，即user发送update=1的确认消息
            msg.setUpdate(1);
            sendTextMessage(msg.getUserId(),JsonUtils.objectToJson(msg));
            msg.setUpdate(0);//别忘改回来
            //
            //如果对方在线 直接发过去一份
            if(CurPool.webSockets.containsKey(msg.getToUserId())){
            sendTextMessage(msg.getToUserId(),JsonUtils.objectToJson(msg));
            }
            //如果对方不在线，给对方更新usersession
            else {
                if (userSessionDao == null){
                    this.userSessionDao = (UserSessionDao) SpringContextUtil.getBean("userSessionDao");
                }
                if (!userSessionDao.findUserSessionByUserIdandToUserId(msg.getToUserId(), msg.getUserId()).isEmpty()) {
                    System.out.println("已存在session");
                }
                else {
                    UserSession us = new UserSession();
                    us.setUserId1(msg.getToUserId());
                    us.setUserId2(msg.getUserId());
                    userSessionDao.saveUserSession(us);
                    System.out.println("创建usersession成功");
                }

            }

        } else if (msg.getUpdate()==3) {
            //用户在线的话就给他发送update=4的消息通知他有好友通过了
            System.out.println("更新消息----------------4");
            if(CurPool.webSockets.containsKey(msg.getToUserId())){
                msg.setUpdate(4);

                System.out.println(msg);
                sendTextMessage(msg.getToUserId(),JsonUtils.objectToJson(msg));
            }
        } else if (msg.getUpdate()==5) {
            //用户在线的话就给他发送update=6的消息通知他有人申请好友
            System.out.println("申请消息----------------5");
            if(CurPool.webSockets.containsKey(msg.getToUserId())){
                msg.setUpdate(6);

                System.out.println(msg);
                sendTextMessage(msg.getToUserId(),JsonUtils.objectToJson(msg));
            }

        }


    }
    // 此为广播消息
//    public void sendAllMessage(String message) {
//        for(WebSocket webSocket : webSockets) {
//            System.out.println("【websocket消息】广播消息:"+message);
//            try {
//                webSocket.session.getAsyncRemote().sendText(message);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    // 此为单点消息 (发送文本)
    public void sendTextMessage(Integer userId, String message) {
        Session session = (Session)CurPool.webSockets.get(userId).session;
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息 (发送对象)
//    public void sendObjMessage(String sessionId, Object message) {
//        Session session = CurPool.sessionPool.get(sessionId);
//        if (session != null) {
//            try {
////                session.getAsyncRemote().sendObject(message);
//                session.getBasicRemote().sendText(JsonUtils.objectToJson(message));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

}