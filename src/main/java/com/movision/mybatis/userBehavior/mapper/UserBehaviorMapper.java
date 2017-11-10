package com.movision.mybatis.userBehavior.mapper;

import com.movision.mybatis.userBehavior.entity.UserBehavior;

import java.util.List;

public interface UserBehaviorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBehavior record);

    int insertSelective(UserBehavior record);

    UserBehavior selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBehavior record);

    int updateByPrimaryKey(UserBehavior record);

    List<UserBehavior> findAllUserBehavior(int userid);
}