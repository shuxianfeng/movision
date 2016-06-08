package com.zhuhuibao.mybatis.witkty.service;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.mybatis.witkty.entity.Cooperation;
import com.zhuhuibao.mybatis.witkty.entity.CooperationType;
import com.zhuhuibao.mybatis.witkty.mapper.CooperationMapper;
import com.zhuhuibao.mybatis.witkty.mapper.CooperationTypeMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/5/4 0004.
 */
@Service
@Transactional
public class CooperationService {
    private static final Logger log = LoggerFactory.getLogger(CooperationService.class);

    @Autowired
    private CooperationMapper cooperationMapper;

    @Autowired
    private CooperationTypeMapper cooperationTypeMapper;
    /**
     * 发布任务
     */
    public int publishCooperation(Cooperation cooperation){
        try{
            return cooperationMapper.publishCooperation(cooperation);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 合作类型(大类，子类)
     */
    public List cooperationType(){
        try{
            List<CooperationType> cooperationTypeList = cooperationTypeMapper.findCooperationType();
            List<CooperationType> subCooperationTypeList = cooperationTypeMapper.findSubCooperationType();
            List list1 = new ArrayList();
            for(int i=0;i<cooperationTypeList.size();i++){
                CooperationType cooperationType = cooperationTypeList.get(i);
                Map map = new HashMap();
                map.put(Constants.code,cooperationType.getId());
                map.put(Constants.name,cooperationType.getName());
                List list = new ArrayList();
                for(int y=0;y<subCooperationTypeList.size();y++){
                    CooperationType subCooperation = subCooperationTypeList.get(y);
                    if(cooperationType.getId().equals(subCooperation.getParentId())){
                        Map map1 = new HashMap();
                        map1.put(Constants.code,subCooperation.getId());
                        map1.put(Constants.name,subCooperation.getName());
                        list.add(map1);
                    }
                }
                map.put("subCooperationList",list);
                list1.add(map);
            }
            return list1;
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 合作类型(子类)
     */
    public List subCooperationType(){
        try{
            List<CooperationType> subCooperationTypeList = cooperationTypeMapper.findSubCooperationType();
            List list = new ArrayList();
            for(int y=0;y<subCooperationTypeList.size();y++){
                CooperationType subCooperation = subCooperationTypeList.get(y);
                Map map = new HashMap();
                map.put(Constants.code,subCooperation.getId());
                map.put(Constants.name,subCooperation.getName());
                list.add(map);
            }
            return list;
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 编辑任务
     */
    public int updateCooperation(Cooperation cooperation){
        try{
            return cooperationMapper.updateCooperation(cooperation);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 更新点击率
     */
    public int updateCooperationViews(Cooperation cooperation){
        try{
            return cooperationMapper.updateCooperationViews(cooperation);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 批量删除任务
     */
    public void deleteCooperation(String ids[]){
        try {
            for(int i=0;i<ids.length;i++){
                String id = ids[i];
                cooperationMapper.deleteCooperation(id);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 查询一条任务的信息
     */
    public Cooperation queryCooperationInfoById(String id){
        try{
            Cooperation cooperation = cooperationMapper.queryCooperationInfoById(id);
            return cooperation;
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 根据条件查询任务信息列表（分页）
     */
    public List<Map<String,String>> findAllCooperationByPager( Paging<Map<String,String>> pager,Cooperation cooperation){
        try {
            List<Map<String,String>> cooperationList = cooperationMapper.findAllCooperationByPager(pager.getRowBounds(),cooperation);
            return cooperationList;
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 最热合作信息
     */
    public List<Cooperation> queryHotCooperation(Map<String,Object> map){
        try {
            return cooperationMapper.queryHotCooperation(map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
