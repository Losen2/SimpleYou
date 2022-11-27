package com.fengshang.im.controller;

import com.fengshang.im.entity.Message;
import com.fengshang.im.entity.UserSession;
import com.fengshang.im.mapper.FriendMapper;
import com.fengshang.im.mapper.MessageDao;
import com.fengshang.im.mapper.UserSessionDao;
import com.fengshang.im.utils.JsonWrapper;
import com.fengshang.im.utils.CurPool;
import com.fengshang.im.mapper.UserMapper;
import com.fengshang.im.entity.User;
import com.fengshang.im.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fengshang.im.utils.JWTUtils;
import com.fengshang.im.entity.LoginReturnPOJO;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserSessionDao userSessionDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private FriendMapper friendMapper;

    // 注册用户
    @GetMapping("/register")
    public JsonWrapper<?> register(@RequestParam String name, @RequestParam String pwd, @RequestParam String pwdrepeat) {
        String s = userMapper.selectByName(name);
        if (s != null || "".equals(s)) {
            return JsonWrapper.failure("该用户已存在");
        }
        if (name == null || pwd == null || pwdrepeat == null) {
            return JsonWrapper.failure("输入不能为空");
        }
        if (!pwd.equals(pwdrepeat)) {
            return JsonWrapper.failure("两次输入密码不一致");//两次pwd一样才能往下走
        }
        User user = new User();
        user.setName(name);
        user.setPwd(CommonUtils.MD5(pwd));
        userMapper.insert(user);
        return JsonWrapper.success("注册成功");
    }


    // 登录
    @GetMapping("/login")
    public JsonWrapper<?> login(@RequestParam String name, @RequestParam String pwd) {

        User user = userMapper.selectUserByName(name);
        if (user == null || user.getId() == null) {
            return JsonWrapper.failure("该用户不存在");
        }
        if (user.getPwd().equals(CommonUtils.MD5(pwd))) {

            String token = JWTUtils.geneJsonWebToken(user);
            System.out.println("登陆成功");

            JsonWrapper<LoginReturnPOJO> jw = JsonWrapper.success("登陆成功");
            LoginReturnPOJO lr = new LoginReturnPOJO(user.getId(), user.getName(), token);
            jw.setData(lr);
            return jw;
        } else {
            return JsonWrapper.success("登录失败");
        }
    }

    // 注销
    @GetMapping("/loginOut")
    public JsonWrapper<?> loginOut(HttpServletRequest request) {
        String name = (String) request.getAttribute("name");
        if (name != null && !"".equals(name)) {

            User user = userMapper.selectUserByName(name);

            CurPool.webSockets.remove(user.getId());
            System.out.println("退出成功");
        }
        return JsonWrapper.success("退出登录成功");
    }

    @GetMapping({"/selectuserbyname"})
    public JsonWrapper<?> selectUserByName(@RequestParam String name) {
        User user = this.userMapper.selectUserByName(name);
        return user == null ? JsonWrapper.failure("用户不存在") : JsonWrapper.success(user);
    }

    @GetMapping({"/selectuserbyid"})
    public JsonWrapper<?> selectUserById(HttpServletRequest request, @RequestParam Integer id) {
        User user = this.userMapper.selectByPrimaryKey(id);
        return user == null ? JsonWrapper.failure("用户不存在") : JsonWrapper.success(user);
    }

    @GetMapping({"/selecttofriend"})//Map<User,isFriend> 0否 1是
    public JsonWrapper<?> selectToFriend(HttpServletRequest request, @RequestParam Integer id) {
        if(userMapper.selectByPrimaryKey(id)==null){
            return JsonWrapper.failure("noPerson");
        };
        User user = this.userMapper.selectByPrimaryKey(id);
        int userId = (int) request.getAttribute("user_id");
        List<User> friendlist = friendMapper.getFriendList(userId);
        JsonWrapper<User> jw = new JsonWrapper<>();
        if(userId == id){
            return JsonWrapper.failure("donotserachself");
        }
        for (User us : friendlist) {
            if (us.getId().equals(id)) {

                jw.setData(us);
                jw.setMsg("好友");
                return jw;
            }
        }

        jw.setData(user);
        jw.setMsg("陌生人");
        return jw;
    }

    @GetMapping({"/updateavaterbyid"})
    public JsonWrapper<?> updateAvaterById(HttpServletRequest request, @RequestParam String avaterUrl) {
        int id = (int) request.getAttribute("user_id");
        if (id != 0) {
            userMapper.updateAvaterById(id, avaterUrl);
        }
        return JsonWrapper.success("设置新头像成功");
    }

    @GetMapping({"/getusersession"})
    public JsonWrapper<?> getAvaterById(HttpServletRequest request) {
        int id = (int) request.getAttribute("user_id");
        JsonWrapper<List<User>> jw = new JsonWrapper<>();
        List<User> usel = new ArrayList<>();
        List<UserSession> ans = userSessionDao.findUserSessionByUserId(id);
        for (UserSession an : ans) {
            usel.add(userMapper.selectByPrimaryKey(an.getUserId2()));
        }
        return JsonWrapper.success("获取会话列表成功", 0, usel);
    }

    @GetMapping({"/createusersession"})
    public JsonWrapper<?> getAvaterById(HttpServletRequest request, @RequestParam Integer touserid) {
        //注意去重
        int id = (int) request.getAttribute("user_id");

        if (!userSessionDao.findUserSessionByUserIdandToUserId(id, touserid).isEmpty()) {
            System.out.println("已存在session");
            return JsonWrapper.failure("已存在session");

        }
        UserSession us = new UserSession();
        us.setUserId1(id);
        us.setUserId2(touserid);
        userSessionDao.saveUserSession(us);
        return JsonWrapper.success("创建usersession成功");
    }

    @GetMapping({"/gettousermessages"})
    public JsonWrapper<?> getMessages(HttpServletRequest request, @RequestParam Integer touserid) {
        //注意去重
        int id = (int) request.getAttribute("user_id");

        return JsonWrapper.success("获取特定touser消息成功", 0, messageDao.getMessage(id, touserid));

    }

    @GetMapping({"/pullrecentmessages"})
    public JsonWrapper<?> pullRecentMessages(HttpServletRequest request, @RequestParam long mongoId){
        //获取最近的500条消息
        //同时，获取所有的未读消息
        int id = (int) request.getAttribute("user_id");
        return JsonWrapper.success("拉取消息成功",0,messageDao.getRecentMessage(id,mongoId));
    }

    @GetMapping({"/clearunreadcount"})
    public JsonWrapper<?> clearUnreadCount(HttpServletRequest request, @RequestParam Integer toUserId){
        //获取最近的500条消息
        //同时，获取所有的未读消息
        int userId = (int) request.getAttribute("user_id");
        messageDao.clearUnreadCount(userId,toUserId);
        return JsonWrapper.success("更新消息已读成功");
    }

}
