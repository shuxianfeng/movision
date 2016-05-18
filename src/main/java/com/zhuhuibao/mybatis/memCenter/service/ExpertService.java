package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.mybatis.memCenter.entity.Achievement;
import com.zhuhuibao.mybatis.memCenter.mapper.AchievementMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 专家业务处理类
 * Created by cxx on 2016/5/17 0017.
 */
@Service
@Transactional
public class ExpertService {
    private static final Logger log = LoggerFactory.getLogger(ExpertService.class);

    @Autowired
    private AchievementMapper achievementMapper;

    /**
     * 发布技术成果
     * @param achievement
     * @return
     */
    public int publishAchievement(Achievement achievement){
        return achievementMapper.publishAchievement(achievement);
    }

    /**
     * 技术成果详情
     * @param id
     * @return
     */
    public Achievement queryAchievementById(String id){
        return achievementMapper.queryAchievementById(id);
    }

    /**
     * 技术成果列表
     */
    public List<Achievement> findAllAchievementList(Paging<Achievement> pager,Map<String,Object> map){
        return achievementMapper.findAllAchievementList(pager.getRowBounds(),map);
    }

    /**
     * 更新技术成果
     */
    public int updateAchievement(Achievement achievement){
        return achievementMapper.updateAchievement(achievement);
    }
}
