package com.fengshang.im.mapper;

import com.fengshang.im.entity.FriendApply;
import com.fengshang.im.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface FriendMapper {
    FriendApply selectByPrimaryKey(Integer id);

    int insert(FriendApply fa);

    int update(FriendApply fa);

    List<FriendApply> selectByapplyId(@Param("apply_id") Integer applyId);

    List<FriendApply> selectByappliedId(@Param("applied_id") Integer appliedId);

    List<FriendApply> selectByapplyandappliedId(@Param("apply_id") Integer applyId,@Param("applied_id") Integer appliedId);

    List<User> getFriendList(@Param("user_id") Integer userId);

    int addFriend(@Param("user_id1") Integer userId1, @Param("user_id2") Integer userId2, @Param("create_time")Date createTime);


}
