package com.movision.mybatis.robotComment.mapper;

import com.movision.mybatis.robotComment.entity.RobotComment;

public interface RobotCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RobotComment record);

    int insertSelective(RobotComment record);

    RobotComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RobotComment record);

    int updateByPrimaryKey(RobotComment record);
}