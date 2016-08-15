package com.zhuhuibao.mybatis.expert.mapper;

import com.zhuhuibao.mybatis.expert.entity.Achievement;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface AchievementMapper {

    int publishAchievement(Achievement achievement);

    Map<String,String> queryAchievementById(String id);

    Map<String,String> queryUnLoginAchievementById(String id);

    //分页
    List<Achievement> findAllAchievementList(RowBounds rowBounds, Map<String, Object> map);

    List<Achievement> findAllAchievementList(Map<String, Object> map);

    List<Map<String,String>> findAchievementListByCount(int count);

    int updateAchievement(Achievement achievement);

    List<Map<String,String>> findAllMyLookedAchievementList(RowBounds rowBounds, Map<String, Object> map);

    int deleteLookedAchievement(String id);
}