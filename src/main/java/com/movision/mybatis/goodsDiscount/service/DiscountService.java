package com.movision.mybatis.goodsDiscount.service;

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
}
