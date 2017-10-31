package com.movision.mybatis.robotOperationJob.mapper;

import com.movision.mybatis.robotOperationJob.entity.RobotOperationJob;

public interface RobotOperationJobMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RobotOperationJob record);

    int insertSelective(RobotOperationJob record);

    RobotOperationJob selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RobotOperationJob record);

    int updateByPrimaryKey(RobotOperationJob record);

    RobotOperationJob selectCurrentPostidBatch(int postid);
}