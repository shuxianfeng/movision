package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.*;
import com.zhuhuibao.mybatis.memCenter.entity.Agent;
import com.zhuhuibao.mybatis.memCenter.mapper.AgentMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.ProvinceMapper;
import com.zhuhuibao.mybatis.oms.entity.Category;
import com.zhuhuibao.mybatis.oms.mapper.CategoryMapper;
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
 * 代理商业务处理
 * Created by cxx on 2016/3/23 0023.
 */
@Service
@Transactional
public class AgentService {
    private static final Logger log = LoggerFactory.getLogger(AgentService.class);

    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     *关联代理商保存
     * @param agent
     * @return
     */
    public int agentSave(Agent agent){
        log.debug("关联代理商保存");
        int isSave = agentMapper.agentSave(agent);
        return isSave;
    }

    /**
     *关联代理商更新编辑
     * @param agent
     * @return
     */
    public int agentUpdate(Agent agent){
        log.debug("关联代理商更新编辑");
        int isUpdate = agentMapper.agentUpdate(agent);
        return isUpdate;
    }

    /**
     *区域按首拼分类
     * @param
     * @return
     */
    public List<CommonBean> searchProvinceByPinYin(){
        log.debug("区域按首拼分类");
        List<CommonBean> list = provinceMapper.searchProvinceByPinYin();
        return list;
    }

    /**
     * 根据会员id查询代理商
     * @param
     * @return
     */
    public List<AgentBean> findAgentByMemId(String id){
        log.debug("根据会员id查询代理商");
        List<AgentBean> list = agentMapper.findAgentByMemId(id);
        return list;
    }

    /**
     * 代理商编辑，参数，id
     * @param
     * @return
     */
    public JsonResult updateAgentById(String id){
        JsonResult result = new JsonResult();
        try{
            AgentBean agent = agentMapper.updateAgentById(id);
            Map map = new HashMap();
            Map map1 = new HashMap();
            map1.put("id",agent.getId());
            map1.put("account",agent.getAccount());
            map1.put("name",agent.getCompany());
            map.put("brandId",agent.getBrand());
            map.put("province",agent.getProvince());
            String agentRange[] = agent.getAgentRange().split(",");
            List list = new ArrayList();
            for(int i=0;i<agentRange.length;i++){
                Map map2 = new HashMap();
                ResultBean result1 = categoryMapper.querySystem(agentRange[i]);
                ResultBean result2 = categoryMapper.querySystemByScateid(agentRange[i]);
                map2.put("id",result1.getCode());
                map2.put("name",result2.getName()+">"+result1.getName());
                list.add(map2);
            }
            map.put("product",list);
            map.put("agent",map1);
            result.setCode(200);
            result.setData(map);
        }catch (Exception e){
            log.error("query error ",e);
            e.printStackTrace();
        }
        return result;
    }

    public Agent find(Agent agent){
        Agent result = agentMapper.find(agent);
        return result;
    }


    public JsonResult getAgentByProId(String id){
        JsonResult result = new JsonResult();
        List<ResultBean> provinceList = provinceMapper.findProvince();
        List<ResultBean> agentList = agentMapper.findAgentByProId(id);
        ResultBean resultBean = agentMapper.findManufactor(id);
        List list = new ArrayList();
        Map map = new HashMap();
        Map map3 = new HashMap();
        map3.put(Constant.id,resultBean.getCode());
        map3.put(Constant.name,resultBean.getName());
        map.put("manufactor",map3);
        for(int i=0;i<provinceList.size();i++){
            ResultBean province = provinceList.get(i);
            Map map1 = new HashMap();
            map1.put(Constant.id,province.getCode());
            map1.put(Constant.name,province.getName());
            List list1 = new ArrayList();
            for(int j=0;j<agentList.size();j++){
                ResultBean agent = agentList.get(j);
                Map map2 = new HashMap();
                if(agent.getSmallIcon().contains(province.getSmallIcon())){
                    map2.put(Constant.id,agent.getCode());
                    map2.put(Constant.name,agent.getName());
                    list1.add(map2);
                }
            }
            map1.put("agentList",list1);
            list.add(map1);
        }
        map.put("agent",list);
        result.setCode(200);
        result.setData(map);
        return result;
    }
}
