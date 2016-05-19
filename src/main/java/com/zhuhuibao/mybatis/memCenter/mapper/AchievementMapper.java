package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Achievement;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface AchievementMapper {

    int publishAchievement(Achievement achievement);

    Achievement queryAchievementById(String id);

    //分页
    List<Achievement> findAllAchievementList(RowBounds rowBounds,Map<String,Object> map);

    List<Achievement> findAllAchievementList(Map<String,Object> map);

    int updateAchievement(Achievement achievement);
}