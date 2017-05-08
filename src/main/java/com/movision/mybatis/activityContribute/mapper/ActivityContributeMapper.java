package com.movision.mybatis.activityContribute.mapper;

import com.movision.mybatis.activityContribute.entity.ActivityContribute;
import com.movision.mybatis.activityContribute.entity.ActivityContributeVo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface ActivityContributeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActivityContribute record);

    int insertSelective(ActivityContribute record);

    ActivityContribute selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActivityContribute record);

    int updateByPrimaryKey(ActivityContribute record);

    List<ActivityContributeVo> findAllQueryActivityContribute(Map map, RowBounds rowBounds);

    ActivityContribute queryContributeExplain(String id);
}