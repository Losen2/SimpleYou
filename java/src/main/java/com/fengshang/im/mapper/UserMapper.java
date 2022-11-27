package com.fengshang.im.mapper;

import com.fengshang.im.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User user);

    int insertSelective(User user);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User user);

    int updateByPrimaryKey(User user);

    String selectByName(String name);

    List<User> getCloudList(@Param("list") List<Integer> list);

    User selectUserByName(@Param("name") String name);
    int updateAvaterById(@Param("id")int id,@Param("url")String url);
}
