package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.*;
import com.zhuhuibao.mybatis.memCenter.entity.Agent;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
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
    public int agentSave(Agent agent)throws Exception{
        try {
            return agentMapper.agentSave(agent);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     *关联代理商更新编辑
     * @param agent
     * @return
     */
    public int agentUpdate(Agent agent)throws Exception{
        try {
            return agentMapper.agentUpdate(agent);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     *区域按首拼分类
     * @param
     * @return
     */
    public List<CommonBean> searchProvinceByPinYin()throws Exception{
        try {
            return provinceMapper.searchProvinceByPinYin();
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 根据会员id查询代理商
     * @param
     * @return
     */
    public List<AgentBean> findAgentByMemId(String id)throws Exception{
        try {
            return agentMapper.findAgentByMemId(id);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 代理商编辑，参数，id
     * @param
     * @return
     */
    public Map updateAgentById(String id)throws Exception{
        try{
            AgentBean agent = agentMapper.updateAgentById(id);
            Map map = new HashMap();
            Map map1 = new HashMap();
            map1.put("id",agent.getId());
            map1.put("account",agent.getAccount());
            map1.put("name",agent.getCompany());
            map.put("brandId",agent.getBrand());
            map.put("brandName",agent.getBrandName());
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
            return map;
        }catch (Exception e){
            throw e;
        }
    }

    public Agent find(Agent agent)throws Exception{
        try {
            return agentMapper.find(agent);
        }catch (Exception e){
            throw e;
        }
    }


    public Map getAgentByProId(String id)throws Exception{
        try {
            List<ResultBean> provinceList = provinceMapper.findProvince();
            List<ResultBean> agentList = agentMapper.findAgentByProId(id);
            Member member = agentMapper.findManufactorByProId(id);
            List list = new ArrayList();
            Map map = new HashMap();
            Map map3 = new HashMap();
            map3.put(Constant.id,member.getId());
            map3.put(Constant.name,member.getEnterpriseName());
            map3.put(Constant.logo,member.getEnterpriseLogo());
            map3.put(Constant.webSite,member.getEnterpriseWebSite());
            map3.put(Constant.address,member.getAddress());
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
                    if(agent.getSmallIcon().contains(province.getCode())){
                        map2.put(Constant.id,agent.getCode());
                        map2.put(Constant.name,agent.getName());
                        list1.add(map2);
                    }
                }
                map1.put("agentList",list1);
                list.add(map1);
            }
            map.put("agent",list);
            return map;
        }catch (Exception e){
            throw e;
        }
    }

    public List<ResultBean> getGreatAgentByScateid(String id)throws Exception{
        try {
            return agentMapper.getGreatAgentByScateid(id);
        }catch (Exception e){
            throw e;
        }
    }

    public List<ResultBean> getGreatAgentByBrandId(String id)throws Exception{
        try {
            return agentMapper.getGreatAgentByBrandId(id);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 根据品牌id查询代理商跟厂商（区域分组）
     */
    public Map getAgentByBrandid(String id)throws Exception{
        try {
            List<ResultBean> provinceList = provinceMapper.findProvince();
            List<ResultBean> agentList = agentMapper.getAgentByBrandid(id);
            ResultBean resultBean = agentMapper.findManufactorByBrandid(id);
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
                    if(agent.getSmallIcon().contains(province.getCode())){
                        map2.put(Constant.id,agent.getCode());
                        map2.put(Constant.name,agent.getName());
                        list1.add(map2);
                    }
                }
                map1.put("agentList",list1);
                list.add(map1);
            }
            map.put("agent",list);
            return map;
        }catch (Exception e){
            throw e;
        }
    }
}
