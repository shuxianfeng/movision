package com.movision.mybatis.logisticsfeeCalculateRule.mapper;

import com.movision.mybatis.logisticsfeeCalculateRule.entity.LogisticsfeeCalculateRule;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface LogisticsfeeCalculateRuleMapper {
    int insert(LogisticsfeeCalculateRule record);

    int insertSelective(LogisticsfeeCalculateRule record);

    LogisticsfeeCalculateRule queryLogisticsfeeCalculateRule(int shopid);

    int updateCarriageCalculate(Map map);

    LogisticsfeeCalculateRule queryCarriageCalculate(String shopid);
}