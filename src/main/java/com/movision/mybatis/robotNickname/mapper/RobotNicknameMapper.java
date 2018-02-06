package com.movision.mybatis.robotNickname.mapper;

import com.movision.mybatis.robotNickname.entity.RobotNickname;

import java.util.List;

public interface RobotNicknameMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RobotNickname record);

    int insertSelective(RobotNickname record);

    RobotNickname selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RobotNickname record);

    int updateByPrimaryKey(RobotNickname record);

    List<String> queryRoboltNickname(Integer number);

    String queryNickname();
}