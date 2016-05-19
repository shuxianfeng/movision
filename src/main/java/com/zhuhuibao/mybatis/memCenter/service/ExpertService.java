package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.mybatis.memCenter.entity.Achievement;
import com.zhuhuibao.mybatis.memCenter.entity.Dynamic;
import com.zhuhuibao.mybatis.memCenter.mapper.AchievementMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.DynamicMapper;
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

    @Autowired
    private DynamicMapper dynamicMapper;

    /**
     * 发布技术成果
     * @param achievement
     * @return
     */
    public int publishAchievement(Achievement achievement)throws Exception
    {
        try{
            return achievementMapper.publishAchievement(achievement);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 技术成果详情
     * @param id
     * @return
     */
    public Achievement queryAchievementById(String id)throws Exception{
        try{
            return achievementMapper.queryAchievementById(id);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 技术成果列表
     */
    public List<Achievement> findAllAchievementList(Paging<Achievement> pager,Map<String,Object> map)throws Exception{
        try{
            return achievementMapper.findAllAchievementList(pager.getRowBounds(),map);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 更新技术成果
     */
    public int updateAchievement(Achievement achievement)throws Exception{
        try{
            return achievementMapper.updateAchievement(achievement);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 发布协会动态
     * @param dynamic
     * @return
     */
    public int publishDynamic(Dynamic dynamic)throws Exception{
        try{
            return dynamicMapper.publishDynamic(dynamic);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 协会动态详情
     * @param id
     * @return
     */
    public Dynamic queryDynamicById(String id)throws Exception{
        try{
            return dynamicMapper.queryDynamicById(id);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 更新动态详情
     */
    public int updateDynamic(Dynamic dynamic)throws Exception{
        try{
            return dynamicMapper.updateDynamic(dynamic);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 动态详情列表
     */
    public List<Dynamic> findAllDynamicList(Paging<Dynamic> pager,Map<String,Object> map)throws Exception{
        try{
            return dynamicMapper.findAllDynamicList(pager.getRowBounds(),map);
        }catch (Exception e){
            throw e;
        }
    }
}
