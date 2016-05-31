package com.zhuhuibao.mybatis.tech.service;
import com.zhuhuibao.mybatis.tech.entity.TechCooperation;
import com.zhuhuibao.mybatis.tech.mapper.TechCooperationMapper;
import com.zhuhuibao.mybatis.tech.mapper.TechDataMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *技术频道业务处理类
 *@author pl
 *@create 2016/5/27 0027
 **/
@Service
@Transactional
public class TechnologyService {

    private final static Logger log = LoggerFactory.getLogger("TechService");

    @Autowired
    TechCooperationMapper techMapper;

    @Autowired
    TechDataMapper techDataMapper;

    /**
     * 插入技术成果或者技术需求
     * @param tech
     * @return
     */
    public int insertTechCooperation(TechCooperation tech)
    {
        int result = 0;
        log.info("insert tech cooperation info "+ StringUtils.beanToString(tech));
        try {
            result = techMapper.insertSelective(tech);
        }catch(Exception e)
        {
            log.error("insert tech cooperation info error!",e);
            throw e;
        }
        return result;
    }

    /**
     * 技术培训频道技术合作搜索
     * @param condition 搜索条件
     * @return
     */
    public List<Map<String,String>> findAllTechCooperationPager(Paging<Map<String,String>> pager, Map<String,Object> condition)
    {
        log.info("find all tech cooperation for pager "+StringUtils.mapToString(condition));
        List<Map<String,String>> techList = null;
        try{
            techList = techMapper.findAllTechCooperationPager(pager.getRowBounds(),condition);
        }catch(Exception e)
        {
            log.error("find all tech cooperation for pager error!",e);
            throw e;
        }
        return techList;
    }

    /**
     * 运营管理平台技术合作搜索
     * @param condition 搜索条件
     * @return
     */
    public List<Map<String,String>> findAllOMSTechCooperationPager(Paging<Map<String,String>> pager,Map<String,Object> condition)
    {
        log.info("find all OMS tech cooperation for pager "+StringUtils.mapToString(condition));
        List<Map<String,String>> techList = null;
        try{
            techList = techMapper.findAllOMSTechCooperationPager(pager.getRowBounds(),condition);
        }catch(Exception e)
        {
            log.error("find all OMS tech cooperation for pager error!",e);
            throw e;
        }
        return techList;
    }

    /**
     * 更新技术合作:成果和需求
     * @param tech
     * @return
     */
    public int updateTechCooperation(TechCooperation tech)
    {
        int result;
        log.info("update oms tech cooperation "+StringUtils.beanToString(tech));
        try{
            result = techMapper.updateByPrimaryKeySelective(tech);
        }catch (Exception e){
            log.error("update oms tech cooperation error! ",e);
            throw e;
        }
        return result;
    }

    /**
     * 注销技术合作:成果和需求
     * @param condition
     * @return
     */
    public int deleteTechCooperation(Map<String, Object> condition)
    {
        int result;
        log.info("delete oms tech cooperation "+StringUtils.mapToString(condition));
        try{
            result = techMapper.deleteTechCooperation(condition);
        }catch (Exception e){
            log.error("delete oms tech cooperation error! ",e);
            throw e;
        }
        return result;
    }

    /**
     * 更新技术合作的点击率
     * @param techCoopId 技术合作ID
     * @return
     */
    public int updateTechCooperationViews(String techCoopId)
    {
        int result;
        log.info("update tech cooperation views "+techCoopId);
        try{
            result = techMapper.updateTechCooperationViews(Long.valueOf(techCoopId));
        }catch (Exception e){
            log.error("update tech cooperation views error! ",e);
            throw e;
        }
        return result;
    }

    /**
     * 根据ID查看技术合作详情
     * @param id 技术合作ID
     * @return
     */
    public TechCooperation selectTechCooperationById(String id)
    {
        TechCooperation techCoop;
        log.info("select tech cooperation by id "+id);
        try{
            techCoop = techMapper.selectByPrimaryKey(Long.valueOf(id));
        }catch (Exception e){
            log.error("select tech cooperation by id error! ",e);
            throw e;
        }
        return techCoop;
    }

    /**
     * 预览技术合作详情
     * @param id 技术合作ID
     * @return
     */
    public TechCooperation previewTechCooperationDetail(String id)
    {
        TechCooperation techCoop;
        log.info("preview tech cooperation by id "+id);
        try{
            techCoop = techMapper.selectByPrimaryKey(Long.valueOf(id));
        }catch (Exception e){
            log.error("select tech cooperation by id error! ",e);
            throw e;
        }
        return techCoop;
    }

}
