package com.movision.mybatis.shop.service;

import com.movision.mybatis.shop.entity.Shop;
import com.movision.mybatis.shop.mapper.ShopMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author shuxf
 * @Date 2017/4/6 20:50
 */
@Service
public class ShopService {

    static private Logger log = LoggerFactory.getLogger(ShopService.class);

    @Autowired
    private ShopMapper shopMapper;

    /**
     * 根据售后单号查询店铺相关信息
     *
     * @param afterserviceid
     * @return
     */
    public Shop queryShopInfo(int afterserviceid) {
        try {
            log.info("根据售后单号查询店铺相关信息");
            return shopMapper.queryShopInfo(afterserviceid);
        } catch (Exception e) {
            log.error("根据售后单号查询店铺相关信息失败");
            throw e;
        }
    }
}
