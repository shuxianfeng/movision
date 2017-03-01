package com.movision.mybatis.goodsDiscount.service;

import com.movision.mybatis.cart.entity.CartVo;
import com.movision.mybatis.goodsDiscount.entity.GoodsDiscountVo;
import com.movision.mybatis.goodsDiscount.mapper.GoodsDiscountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/2/28 13:42
 */
@Service
public class DiscountService {

    private static Logger log = LoggerFactory.getLogger(DiscountService.class);

    @Autowired
    private GoodsDiscountMapper goodsDiscountMapper;

    public List<GoodsDiscountVo> querygoodsDiscount() {
        try {
            log.info("查询当前商品的所有活动列表");
            return goodsDiscountMapper.querygoodsDiscount();
        } catch (Exception e) {
            log.error("查询当前商品的所有活动列表失败");
            throw e;
        }
    }

    public CartVo queryDiscountName(int discountid) {
        try {
            log.info("根据商品优惠活动id查询活动的折扣和活动名称");
            return goodsDiscountMapper.queryDiscountName(discountid);
        } catch (Exception e) {
            log.error("根据商品优惠活动id查询活动的折扣和活动名称失败");
            throw e;
        }
    }
}
