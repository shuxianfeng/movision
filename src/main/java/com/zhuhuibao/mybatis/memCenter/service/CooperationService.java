package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.constant.Constant;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Cooperation;
import com.zhuhuibao.mybatis.memCenter.entity.CooperationType;
import com.zhuhuibao.mybatis.memCenter.mapper.CooperationMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.CooperationTypeMapper;
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
    public int publishCooperation(Cooperation cooperation)throws Exception{
        try{
            return cooperationMapper.publishCooperation(cooperation);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 合作类型(大类，子类)
     */
    public List cooperationType()throws Exception{
        try{
            List<CooperationType> cooperationTypeList = cooperationTypeMapper.findCooperationType();
            List<CooperationType> subCooperationTypeList = cooperationTypeMapper.findSubCooperationType();
            List list1 = new ArrayList();
            for(int i=0;i<cooperationTypeList.size();i++){
                CooperationType cooperationType = cooperationTypeList.get(i);
                Map map = new HashMap();
                map.put(Constant.code,cooperationType.getId());
                map.put(Constant.name,cooperationType.getName());
                List list = new ArrayList();
                for(int y=0;y<subCooperationTypeList.size();y++){
                    CooperationType subCooperation = subCooperationTypeList.get(y);
                    if(cooperationType.getId().equals(subCooperation.getParentId())){
                        Map map1 = new HashMap();
                        map1.put(Constant.code,subCooperation.getId());
                        map1.put(Constant.name,subCooperation.getName());
                        list.add(map1);
                    }
                }
                map.put("subCooperationList",list);
                list1.add(map);
            }
            return list1;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 合作类型(子类)
     */
    public List subCooperationType()throws Exception{
        try{
            List<CooperationType> subCooperationTypeList = cooperationTypeMapper.findSubCooperationType();
            List list = new ArrayList();
            for(int y=0;y<subCooperationTypeList.size();y++){
                CooperationType subCooperation = subCooperationTypeList.get(y);
                Map map = new HashMap();
                map.put(Constant.code,subCooperation.getId());
                map.put(Constant.name,subCooperation.getName());
                list.add(map);
            }
            return list;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 项目类别
     */
    public List cooperationCategory()throws Exception {
        try{
            List<ResultBean> resultBeanList = cooperationMapper.cooperationCategory();
            List list = new ArrayList();
            for(int i=0;i<resultBeanList.size();i++){
                ResultBean resultBean = resultBeanList.get(i);
                Map map = new HashMap();
                map.put(Constant.code,resultBean.getCode());
                map.put(Constant.name,resultBean.getName());
                list.add(map);
            }
            return list;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 编辑任务
     */
    public int updateCooperation(Cooperation cooperation)throws Exception{
        try{
            return cooperationMapper.updateCooperation(cooperation);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 批量删除任务
     */
    public void deleteCooperation(String ids[])throws Exception{
        try {
            for(int i=0;i<ids.length;i++){
                String id = ids[i];
                cooperationMapper.deleteCooperation(id);
            }
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 查询一条任务的信息
     */
    public Cooperation queryCooperationInfoById(String id)throws Exception{
        try{
            Cooperation cooperation = cooperationMapper.queryCooperationInfoById(id);
            return cooperation;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 根据条件查询任务信息列表（分页）
     */
    public List<Cooperation> findAllCooperationByPager( Paging<Cooperation> pager,Cooperation cooperation)throws Exception{
        try {
            List<Cooperation> cooperationList = cooperationMapper.findAllCooperationByPager(pager.getRowBounds(),cooperation);
            return cooperationList;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 最热合作信息
     */
    public List<Cooperation> queryHotCooperation(Map<String,Object> map)throws Exception{
        try {
            return cooperationMapper.queryHotCooperation(map);
        }catch (Exception e){
            throw e;
        }
    }
}
