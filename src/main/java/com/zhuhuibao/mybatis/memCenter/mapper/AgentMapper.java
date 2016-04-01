package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.AgentBean;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Agent;

import java.util.List;

public interface AgentMapper {
    int agentSave(Agent agent);

    int agentUpdate(Agent agent);

    List<AgentBean> findAgentByMemId(String id);

    List<ResultBean> findAgent();

    AgentBean updateAgentById(String id);
}