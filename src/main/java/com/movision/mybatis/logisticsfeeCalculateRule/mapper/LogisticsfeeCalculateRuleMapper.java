package com.movision.mybatis.logisticsfeeCalculateRule.mapper;

import com.movision.mybatis.logisticsfeeCalculateRule.entity.LogisticsfeeCalculateRule;

public interface LogisticsfeeCalculateRuleMapper {
    int insert(LogisticsfeeCalculateRule record);

    int insertSelective(LogisticsfeeCalculateRule record);

    LogisticsfeeCalculateRule queryLogisticsfeeCalculateRule(int shopid);
}