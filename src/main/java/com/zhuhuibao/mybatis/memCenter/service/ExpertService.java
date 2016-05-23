package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.mybatis.memCenter.entity.Achievement;
import com.zhuhuibao.mybatis.memCenter.entity.Dynamic;
import com.zhuhuibao.mybatis.memCenter.entity.Expert;
import com.zhuhuibao.mybatis.memCenter.entity.Question;
import com.zhuhuibao.mybatis.memCenter.mapper.AchievementMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.DynamicMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.ExpertMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.QuestionMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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

    @Autowired
    private ExpertMapper expertMapper;

    @Autowired
    private QuestionMapper questionMapper;

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
     * 技术成果列表(分页)
     */
    public List<Achievement> findAllAchievementList(Paging<Achievement> pager,Map<String,Object> map)throws Exception{
        try{
            return achievementMapper.findAllAchievementList(pager.getRowBounds(),map);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 技术成果列表
     */
    public List<Achievement> findAchievementList(Map<String,Object> map)throws Exception{
        try{
            return achievementMapper.findAllAchievementList(map);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 技术成果列表(控制条数)
     */
    public List<Map<String,String>> findAchievementListByCount(int count)throws Exception{
        try{
            return achievementMapper.findAchievementListByCount(count);
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
     * 更新协会动态
     */
    public int updateDynamic(Dynamic dynamic)throws Exception{
        try{
            return dynamicMapper.updateDynamic(dynamic);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 协会动态列表
     */
    public List<Dynamic> findAllDynamicList(Paging<Dynamic> pager,Map<String,Object> map)throws Exception{
        try{
            return dynamicMapper.findAllDynamicList(pager.getRowBounds(),map);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 协会动态列表(控制条数)
     */
    public List<Map<String,String>> findDynamicListByCount(int count)throws Exception{
        try{
            return dynamicMapper.findDynamicListByCount(count);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 申请专家
     * @param expert
     * @return
     */
    public int applyExpert(Expert expert)throws Exception{
        try{
            return expertMapper.expertMapper(expert);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 专家详情
     * @param id
     * @return
     */
    public Expert queryExpertById(String id)throws Exception{
        try{
            return expertMapper.queryExpertById(id);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 更新专家信息
     * @param expert
     * @return
     */
    public int updateExpert(Expert expert)throws Exception{
        try{
            return expertMapper.updateExpert(expert);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 专家列表
     * @param pager,map
     * @return
     */
    public List<Expert> findAllExpertList(Paging<Expert> pager,Map<String,Object> map)throws Exception{
        try{
            return expertMapper.findAllExpertList(pager.getRowBounds(),map);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 热门专家
     * @param count
     * @return
     */
    public List<Expert> queryHotExpert(int count)throws Exception{
        try{
            return expertMapper.queryHotExpert(count);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 最新专家
     * @param count
     * @return
     */
    public List<Expert> queryLatestExpert(int count)throws Exception{
        try{
            return expertMapper.queryLatestExpert(count);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 向专家咨询
     * @param question
     * @return
     */
    public int askExpert(Question question)throws Exception{
        try{
            return questionMapper.askExpert(question);
        }catch (Exception e){
            throw e;
        }
    }
}
