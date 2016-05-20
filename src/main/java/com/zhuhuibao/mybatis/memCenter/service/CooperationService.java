package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.Constant;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Cooperation;
import com.zhuhuibao.mybatis.memCenter.entity.CooperationType;
import com.zhuhuibao.mybatis.memCenter.entity.Expert;
import com.zhuhuibao.mybatis.memCenter.entity.Position;
import com.zhuhuibao.mybatis.memCenter.mapper.CooperationMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.CooperationTypeMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.access.EjbAccessException;
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
    public JsonResult publishCooperation(Cooperation cooperation){
        JsonResult jsonResult = new JsonResult();
        try{
            cooperationMapper.publishCooperation(cooperation);
        }catch (Exception e){
            log.error("发布失败");
        }
        return jsonResult;
    }

    /**
     * 合作类型(大类，子类)
     */
    public JsonResult cooperationType(){
        JsonResult jsonResult = new JsonResult();
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
        jsonResult.setCode(200);
        jsonResult.setData(list1);
        return jsonResult;
    }

    /**
     * 合作类型(子类)
     */
    public JsonResult subCooperationType(){
        JsonResult jsonResult = new JsonResult();
        List<CooperationType> subCooperationTypeList = cooperationTypeMapper.findSubCooperationType();
        List list = new ArrayList();
        for(int y=0;y<subCooperationTypeList.size();y++){
            CooperationType subCooperation = subCooperationTypeList.get(y);
            Map map = new HashMap();
            map.put(Constant.code,subCooperation.getId());
            map.put(Constant.name,subCooperation.getName());
            list.add(map);
        }
        jsonResult.setCode(200);
        jsonResult.setData(list);
        return jsonResult;
    }

    /**
     * 项目类别
     */
    public JsonResult cooperationCategory() {
        JsonResult jsonResult = new JsonResult();
        List<ResultBean> resultBeanList = cooperationMapper.cooperationCategory();
        List list = new ArrayList();
        for(int i=0;i<resultBeanList.size();i++){
            ResultBean resultBean = resultBeanList.get(i);
            Map map = new HashMap();
            map.put(Constant.code,resultBean.getCode());
            map.put(Constant.name,resultBean.getName());
            list.add(map);
        }
        jsonResult.setData(list);
        return jsonResult;
    }

    /**
     * 编辑任务
     */
    public JsonResult updateCooperation(Cooperation cooperation){
        JsonResult jsonResult = new JsonResult();
        try{
            cooperationMapper.updateCooperation(cooperation);
        }catch (Exception e){
            log.error("编辑失败");
        }
        return jsonResult;
    }

    /**
     * 批量删除任务
     */
    public JsonResult deleteCooperation(String ids[]){
        JsonResult jsonResult = new JsonResult();
        for(int i=0;i<ids.length;i++){
            String id = ids[i];
            cooperationMapper.deleteCooperation(id);
        }
        return jsonResult;
    }

    /**
     * 查询一条任务的信息
     */
    public JsonResult queryCooperationInfoById(String id){
        JsonResult jsonResult = new JsonResult();
        Cooperation cooperation = cooperationMapper.queryCooperationInfoById(id);
        jsonResult.setData(cooperation);
        return jsonResult;
    }

    /**
     * 根据条件查询任务信息列表（分页）
     */
    public JsonResult findAllCooperationByPager( Paging<Cooperation> pager,Cooperation cooperation){
        JsonResult jsonResult = new JsonResult();
        List<Cooperation> cooperationList = cooperationMapper.findAllCooperationByPager(pager.getRowBounds(),cooperation);
        pager.result(cooperationList);
        jsonResult.setData(pager);
        return jsonResult;
    }

    /**
     * 最热服务
     */
    public List<Cooperation> queryHotCooperation(Map<String,Object> map)throws Exception{
        try {
            return cooperationMapper.queryHotCooperation(map);
        }catch (Exception e){
            throw e;
        }
    }
}
