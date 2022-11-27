package com.fengshang.im.controller;

import com.fengshang.im.entity.FriendApply;
import com.fengshang.im.entity.User;
import com.fengshang.im.mapper.FriendMapper;
import com.fengshang.im.mapper.UserMapper;
import com.fengshang.im.utils.JsonWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
public class FriendController {
    @Autowired
    private FriendMapper friendMapper;

    @Autowired
    private UserMapper userMapper;
    @GetMapping("/getfriendlist")
    public JsonWrapper<?> getFriendList(HttpServletRequest request) {
        int id = (int) request.getAttribute("user_id");
        JsonWrapper<List> jw = new JsonWrapper<>();
        if (id != 0) {
            List<User> ans = friendMapper.getFriendList(id);
            jw.setData(ans);
        }
        jw.setMsg("成功");
        return jw;
    }
    @GetMapping("/getfriendapplylist")//获得申请名单,分为收到的和发出的
    public JsonWrapper<?> getFriendApplyList(HttpServletRequest request) {
        int id = (int) request.getAttribute("user_id");
        JsonWrapper<List<FriendApply>> jw = new JsonWrapper<>();

            List<FriendApply> ans = friendMapper.selectByapplyId(id);
            for(FriendApply fa:ans){
                fa.setApplyUser(userMapper.selectByPrimaryKey(fa.getApplyUserId()));
                fa.setAppliedUser(userMapper.selectByPrimaryKey(fa.getAppliedUserId()));

            }
            jw.setData(ans);

        jw.setMsg("成功获取applylist");
        return jw;
    }
    @GetMapping("/getfriendappliedlist")//获得申请名单,分为收到的和发出的
    public JsonWrapper<?> getFriendAppliedList(HttpServletRequest request) {
        int id = (int) request.getAttribute("user_id");
        JsonWrapper<List> jw = new JsonWrapper<>();

        List<FriendApply> ans = friendMapper.selectByappliedId(id);
        for(FriendApply fa:ans){
            fa.setApplyUser(userMapper.selectByPrimaryKey(fa.getApplyUserId()));
            fa.setAppliedUser(userMapper.selectByPrimaryKey(fa.getAppliedUserId()));

        }
        jw.setData(ans);

        jw.setMsg("成功获取appliedlist");
        return jw;
    }

    @GetMapping("/applyforid")//添加好友
    public JsonWrapper<?> applyForId(HttpServletRequest request,Integer toUserId) {
        int userId = (int) request.getAttribute("user_id");
        //先检查用户是否存在，理论上不会出现，但是防止前端篡改乱发
        if(userMapper.selectByPrimaryKey(toUserId)==null){
            return JsonWrapper.failure("noPerson");
        };
        //再检查是否已经发过了且未响应
        List<FriendApply> ansList = friendMapper.selectByapplyandappliedId(userId,toUserId);
        if(ansList.size()!=0&&ansList.get(0).getAcceptOrRefuse()==-1
        ){//表面上会有indexoutofarray 实际应该不会
            return JsonWrapper.failure("applyExist");
        }
        User ans = (User)userMapper.selectByPrimaryKey(toUserId);
        //理论上不会出现，但是防止前端篡改乱发
        List<User> friendlist = friendMapper.getFriendList(userId);
        for(User us:friendlist){
            if(us.getId()==toUserId)
            {return JsonWrapper.failure("isAlreadyFriend");}
        }

        FriendApply fa = new FriendApply();
        fa.setApplyUserId(userId);
        fa.setAppliedUserId(toUserId);
        fa.setCreateTime(new Date());
        fa.setAcceptOrRefuse(-1);//-1表示未读 0拒绝 1同意
        friendMapper.insert(fa);

        return JsonWrapper.success("请求已发送");



    }
    @GetMapping("/receiveapply")//同意添加好友
    public JsonWrapper<?> receiveApply(HttpServletRequest request,int friendApplyId) {
        //改申请列表后改好友列表
        FriendApply fa = friendMapper.selectByPrimaryKey(friendApplyId);
        fa.setAcceptOrRefuse(1);
        friendMapper.update(fa);
        //添加好友
        friendMapper.addFriend(fa.getApplyUserId(),fa.getAppliedUserId(),new Date());
        friendMapper.addFriend(fa.getAppliedUserId(),fa.getApplyUserId(),new Date());
        return JsonWrapper.success("接受申请并添加好友成功");
    }

    @GetMapping("/rejectapply")//拒绝添加好友
    public JsonWrapper<?> rejectApply(HttpServletRequest request,int friendApplyId) {
        FriendApply fa = friendMapper.selectByPrimaryKey(friendApplyId);
        fa.setAcceptOrRefuse(0);
        friendMapper.update(fa);
        return JsonWrapper.success("拒绝添加好友成功");
    }

}
