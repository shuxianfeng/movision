package com.movision.mybatis.userDauStatistics.mapper;

import com.movision.mybatis.userDauStatistics.entity.UserDauStatistics;
import com.movision.mybatis.userDauStatistics.entity.UserDauStatisticsVo;

import java.util.List;
import java.util.Map;

public interface UserDauStatisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDauStatistics record);

    int insertSelective(UserDauStatistics record);

    UserDauStatistics selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserDauStatistics record);

    int updateByPrimaryKey(UserDauStatistics record);

    List<UserDauStatisticsVo> queryUserStatistics(Map map);

    UserDauStatisticsVo queryUserStatisticsGather(Map map);
}