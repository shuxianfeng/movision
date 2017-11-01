package com.movision.mybatis.robotOperationJob.mapper;

import com.movision.mybatis.robotOperationJob.entity.RobotOperationJob;
import com.movision.mybatis.robotOperationJob.entity.RobotOperationJobPage;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface RobotOperationJobMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RobotOperationJob record);

    int insertSelective(RobotOperationJob record);

    RobotOperationJob selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RobotOperationJob record);

    int updateByPrimaryKey(RobotOperationJob record);

    RobotOperationJob selectCurrentPostidBatch(Map map);

    RobotOperationJob selectCurrentUseridBatch(Map map);

    List<RobotOperationJobPage> findAllRobotJobPage(Map map, RowBounds rowBounds);
}