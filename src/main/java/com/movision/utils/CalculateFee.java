package com.movision.utils;

import com.movision.mybatis.cart.entity.CartVo;
import com.movision.mybatis.logisticsfeeCalculateRule.entity.LogisticsfeeCalculateRule;
import com.movision.mybatis.shopAddress.entity.ShopAddress;
import com.movision.mybatis.shopAddress.service.ShopAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/3/7 18:54
 */
@Service
public class CalculateFee {
    private static Logger log = LoggerFactory.getLogger(CalculateFee.class);

    @Autowired
    private ShopAddressService shopAddressService;

    public Map<String, Object> GetFee(List<CartVo> cartVoList, BigDecimal lng, BigDecimal lat) {
        Map<String, Object> feemap = new HashMap<>();
        List<Integer> shopidlist = new ArrayList();//存放所有去重后的shopid

        //对所有的shopid遍历去重
        for (int i = 0; i < cartVoList.size(); i++) {
            int flag = 0;
            for (int j = 0; j < shopidlist.size(); j++) {
                if (cartVoList.get(i).getShopid() == shopidlist.get(j)) {
                    flag = 1;
                }
            }
            if (flag == 0) {
                shopidlist.add(cartVoList.get(i).getShopid());
            }
        }

        //对获取到的所有店铺id进行遍历
        List<Integer> billingShopidList = new ArrayList<>();//用于存放包含租赁商品的店铺id
        for (int i = 0; i < shopidlist.size(); i++) {
            for (int j = 0; j < cartVoList.size(); j++) {
                if (shopidlist.get(i) == cartVoList.get(j).getShopid() && cartVoList.get(j).getType() == 0) {
                    billingShopidList.add(shopidlist.get(i));
                }
            }
        }
        //对billingShopidList做排重
        List<Integer> billingShopid = new ArrayList<>();
        for (int i = 0; i < billingShopidList.size(); i++) {
            int flag = 0;
            for (int j = 0; j < billingShopid.size(); j++) {
                if (billingShopidList.get(i) == billingShopid.get(j)) {
                    flag = 1;
                }
            }
            if (flag == 0) {
                billingShopid.add(billingShopidList.get(i));
            }
        }

        //遍历需要计算运费的店铺
        long totalfee = 0;
        for (int i = 0; i < billingShopid.size(); i++) {
            double fee = 0;
            log.info("需要计算运费的店铺id>>>>>>>>>>>" + billingShopid.get(i));
            //根据shopid查询店铺经纬度
            ShopAddress shopAddress = shopAddressService.queryShopAddressByShopid(billingShopid.get(i));
            double shoplng = shopAddress.getLng().doubleValue();
            double shoplat = shopAddress.getLat().doubleValue();
            //根据经纬度计算两个地点的运费
            double distance = CalculateDistance.GetDistance(shoplng, shoplat, lng.doubleValue(), lat.doubleValue());

            //取出运费规则
            LogisticsfeeCalculateRule logisticsfeeCalculateRule = shopAddressService.queryLogisticsfeeCalculateRule(billingShopid.get(i));

            if (logisticsfeeCalculateRule != null) {
                double startprice = logisticsfeeCalculateRule.getStartprice();//起步价
                double startdistance = logisticsfeeCalculateRule.getStartdistance();//起步公里数
                double beyondbilling = logisticsfeeCalculateRule.getBeyondbilling();//超出每公里多少钱

                if (distance / 1000 <= startdistance) {
                    fee = startprice;
                } else if (distance / 1000 > startdistance) {
                    fee = startprice + (distance / 1000 - startdistance) * beyondbilling;
                }
                //如果计算出的该店铺的运费超出卖家设置的上限，就取上限
                if (fee > logisticsfeeCalculateRule.getCapping()) {
                    fee = logisticsfeeCalculateRule.getCapping();
                }
                long l = Math.round(fee);//向上取整
                feemap.put(billingShopid.get(i).toString(), l);
                totalfee = totalfee + l;
            } else {
                feemap.put(billingShopid.get(i).toString(), "该卖家未设置运费规则");
            }
        }
        feemap.put("totalfee", totalfee);
        return feemap;
    }
}
