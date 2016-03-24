package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Agent;

public interface AgentMapper {
    int agentSave(Agent agent);

    int agentUpdate(Agent agent);
}