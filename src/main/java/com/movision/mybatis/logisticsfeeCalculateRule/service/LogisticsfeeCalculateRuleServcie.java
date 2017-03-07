package com.movision.mybatis.logisticsfeeCalculateRule.service;

import com.movision.mybatis.logisticsfeeCalculateRule.mapper.LogisticsfeeCalculateRuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/3/6 21:13
 */
@Service
public class LogisticsfeeCalculateRuleServcie {

    Logger logger = LoggerFactory.getLogger(LogisticsfeeCalculateRuleServcie.class);

    @Autowired
    LogisticsfeeCalculateRuleMapper logisticsfeeCalculateRuleMapper;

    /**
     * 运费计算规则
     *
     * @param map
     * @return
     */
    public int updateCarriageCalculate(Map map) {
        try {
            logger.info("运费计算规则");
            return logisticsfeeCalculateRuleMapper.updateCarriageCalculate(map);
        } catch (Exception e) {
            logger.error("运费计算规则异常");
            throw e;
        }
    }
}
