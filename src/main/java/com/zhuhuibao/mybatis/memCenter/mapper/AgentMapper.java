package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.pojo.AgentBean;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Agent;
import com.zhuhuibao.mybatis.memCenter.entity.Member;

import java.util.List;

public interface AgentMapper {
    int agentSave(Agent agent);

    int agentUpdate(Agent agent);

    List<AgentBean> findAgentByMemId(String id);

    List<ResultBean> getAgentByBrandid(String id);

    AgentBean selectAgentById(String id);

    Agent find(Agent agent);

    List<ResultBean> findAgentByProId(String id);

    Member findManufactorByProId(String id);

    ResultBean findManufactorByBrandid(String id);

    List<ResultBean> getGreatAgentByScateid(String id);

    List<ResultBean> getGreatAgentVIPByBrandId(String id);
}