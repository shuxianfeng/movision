package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.pojo.AgentBean;
import com.zhuhuibao.common.pojo.CommonBean;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Agent;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.mapper.AgentMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.ProvinceMapper;
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
        try {
            return agentMapper.agentSave(agent);
        }catch (Exception e){
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    /**
     *关联代理商更新编辑
     * @param agent
     * @return
     */
    public int agentUpdate(Agent agent){
        try {
            return agentMapper.agentUpdate(agent);
        }catch (Exception e){
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    /**
     *区域按首拼分类
     * @return
     */
    public List<CommonBean> searchProvinceByPinYin(){
        try {
            return provinceMapper.searchProvinceByPinYin();
        }catch (Exception e){
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    /**
     * 根据会员id查询代理商
     * @param  id
     * @return
     */
    public List<AgentBean> findAgentByMemId(String id){
        try {
            return agentMapper.findAgentByMemId(id);
        }catch (Exception e){
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    /**
     * 代理商编辑，参数，id
     * @param   id
     * @return
     */
    public Map getAgentById(String id){
        try{
            AgentBean agent = agentMapper.selectAgentById(id);
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
            for (String anAgentRange : agentRange) {
                Map map2 = new HashMap();
                ResultBean result1 = categoryMapper.querySystem(anAgentRange);
                ResultBean result2 = categoryMapper.querySystemByScateid(anAgentRange);
                map2.put("id", result1.getCode());
                map2.put("name", result2.getName() + ">" + result1.getName());
                list.add(map2);
            }
            map.put("product",list);
            map.put("agent",map1);
            return map;
        }catch (Exception e){
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public Agent find(Agent agent){
        try {
            return agentMapper.find(agent);
        }catch (Exception e){
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    /**
     * 根据产品id查询代理商跟厂商（区域分组）
     * @param id
     * @return
     */
    public Map getAgentByProId(String id){
        try {
            List<ResultBean> provinceList = provinceMapper.findProvince();
            List<ResultBean> agentList = agentMapper.findAgentByProId(id);
            Member member = agentMapper.findManufactorByProId(id);
            List list = new ArrayList();
            Map map = new HashMap();
            Map map3 = new HashMap();
            if(member != null){
                map3.put(Constants.id,member.getId());
                map3.put(Constants.name,member.getEnterpriseName());
                map3.put(Constants.logo,member.getEnterpriseLogo());
                map3.put(Constants.webSite,member.getEnterpriseWebSite());
                map3.put(Constants.address,member.getAddress());
            }
            map.put("manufactor",map3);

            for (ResultBean province : provinceList) {
                Map map1 = new HashMap();
                map1.put(Constants.id, province.getCode());
                map1.put(Constants.name, province.getName());
                List list1 = new ArrayList();
                for (ResultBean agent : agentList) {
                    Map map2 = new HashMap();
                    if (agent.getSmallIcon().contains(province.getCode())) {
                        map2.put(Constants.id, agent.getCode());
                        map2.put(Constants.name, agent.getName());
                        list1.add(map2);
                    }
                }
                map1.put("agentList", list1);
                list.add(map1);
            }
            map.put("agent",list);
            return map;
        }catch (Exception e){
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public List<ResultBean> getGreatAgentByScateid(String id){
        try {
            return agentMapper.getGreatAgentByScateid(id);
        }catch (Exception e){
            log.error("执行异常>>>",e);
            throw e;
        }
    }


    /**
     * 根据品牌id查询代理商跟厂商（区域分组）
     */
    public Map getAgentByBrandid(String id){
        try {
            List<ResultBean> provinceList = provinceMapper.findProvince();
            List<ResultBean> agentList = agentMapper.getAgentByBrandid(id);
            ResultBean resultBean = agentMapper.findManufactorByBrandid(id);
            List list = new ArrayList();
            Map map = new HashMap();
            Map map3 = new HashMap();
            map3.put(Constants.id,resultBean.getCode());
            map3.put(Constants.name,resultBean.getName());
            map.put("manufactor",map3);
            for (ResultBean province : provinceList) {
                Map map1 = new HashMap();
                map1.put(Constants.id, province.getCode());
                map1.put(Constants.name, province.getName());
                List list1 = new ArrayList();
                for (ResultBean agent : agentList) {
                    Map map2 = new HashMap();
                    if (agent.getSmallIcon().contains(province.getCode())) {
                        map2.put(Constants.id, agent.getCode());
                        map2.put(Constants.name, agent.getName());
                        list1.add(map2);
                    }
                }
                map1.put("agentList", list1);
                list.add(map1);
            }
            map.put("agent",list);
            return map;
        }catch (Exception e){
            log.error("执行异常>>>",e);
            throw e;
        }
    }


    /**
     *根据品牌Id查询VIP代理商
     */
    public List<ResultBean> getGreatAgentVIPByBrandId(String id){
        try {
            return agentMapper.getGreatAgentVIPByBrandId(id);
        }catch (Exception e){
            log.error("执行异常>>>",e);
            throw e;
        }
    }

}
