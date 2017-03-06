package com.movision.mybatis.shopAddress.service;

import com.movision.mybatis.logisticsfeeCalculateRule.entity.LogisticsfeeCalculateRule;
import com.movision.mybatis.logisticsfeeCalculateRule.mapper.LogisticsfeeCalculateRuleMapper;
import com.movision.mybatis.shopAddress.entity.ShopAddress;
import com.movision.mybatis.shopAddress.mapper.ShopAddressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author shuxf
 * @Date 2017/3/6 14:34
 */
@Service
public class ShopAddressService {

    private static Logger log = LoggerFactory.getLogger(ShopAddressService.class);

    @Autowired
    private ShopAddressMapper shopAddressMapper;

    @Autowired
    private LogisticsfeeCalculateRuleMapper logisticsfeeCalculateRuleMapper;

    @Transactional
    public void saveShopAddress(ShopAddress shopAddress) {
        try {
            log.info("保存或更新管理员设置的店铺地址和经纬度");
            shopAddressMapper.delShopAddress(-1);//shopid==-1默认为幻棱自营店铺
            shopAddressMapper.saveShopAddress(shopAddress);
        } catch (Exception e) {
            log.error("保存管理员设置的店铺地址和经纬度失败");
            throw e;
        }
    }

    public ShopAddress queryShopAddressByShopid(int shopid) {
        try {
            log.info("根据店铺id查询店铺地址和经纬度");
            return shopAddressMapper.queryShopAddressByShopid(shopid);
        } catch (Exception e) {
            log.error("根据店铺id查询店铺地址和经纬度失败");
            throw e;
        }
    }

    public LogisticsfeeCalculateRule queryLogisticsfeeCalculateRule(int shopid) {
        try {
            log.info("根据店铺id查询运费的计费规则");
            return logisticsfeeCalculateRuleMapper.queryLogisticsfeeCalculateRule(shopid);
        } catch (Exception e) {
            log.error("根据店铺id查询运费的计费规则失败");
            throw e;
        }
    }
}
