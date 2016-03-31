package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.AgentBean;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Agent;
import com.zhuhuibao.mybatis.memCenter.mapper.AgentMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.ProvinceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<ResultBean> searchProvinceByPinYin(){
        log.debug("区域按首拼分类");
        List<ResultBean> list = provinceMapper.searchProvinceByPinYin();
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

}
