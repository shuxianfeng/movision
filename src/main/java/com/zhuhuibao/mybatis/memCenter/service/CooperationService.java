package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.Constant;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Cooperation;
import com.zhuhuibao.mybatis.memCenter.entity.CooperationType;
import com.zhuhuibao.mybatis.memCenter.entity.Position;
import com.zhuhuibao.mybatis.memCenter.mapper.CooperationMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.CooperationTypeMapper;
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
     * 合作类型
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
}
