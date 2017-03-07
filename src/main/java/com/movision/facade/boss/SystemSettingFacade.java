package com.movision.facade.boss;

import com.movision.mybatis.logisticsfeeCalculateRule.entity.LogisticsfeeCalculateRule;
import com.movision.mybatis.logisticsfeeCalculateRule.service.LogisticsfeeCalculateRuleServcie;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/3/6 21:10
 */
@Service
public class SystemSettingFacade {
    @Autowired
    LogisticsfeeCalculateRuleServcie logisticsfeeCalculateRuleServcie;

    /**
     * 运费计算规则
     *
     * @param shopid
     * @param startprice
     * @param startdistance
     * @param beyondbilling
     * @return
     */
    public int updateCarriageCalculate(String shopid, String startprice, String startdistance, String beyondbilling) {
        Map map = new HashedMap();
        map.put("shopid", shopid);
        map.put("startprice", startprice);
        map.put("startdistance", startdistance);
        map.put("beyondbilling", beyondbilling);
        map.put("intime", new Date());//最后修改时间
        return logisticsfeeCalculateRuleServcie.updateCarriageCalculate(map);
    }

    /**
     * 查询运费计算规则
     *
     * @param shopid
     * @return
     */
    public LogisticsfeeCalculateRule queryCarriageCalculate(String shopid) {
        return logisticsfeeCalculateRuleServcie.queryCarriageCalculate(shopid);
    }
}
