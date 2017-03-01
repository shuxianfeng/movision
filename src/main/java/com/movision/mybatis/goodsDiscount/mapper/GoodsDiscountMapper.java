package com.movision.mybatis.goodsDiscount.mapper;

import com.movision.mybatis.cart.entity.CartVo;
import com.movision.mybatis.goodsDiscount.entity.GoodsDiscount;
import com.movision.mybatis.goodsDiscount.entity.GoodsDiscountVo;

import java.util.List;

public interface GoodsDiscountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsDiscount record);

    int insertSelective(GoodsDiscount record);

    GoodsDiscount selectByPrimaryKey(Integer id);

    CartVo queryDiscountName(int discountid);

    List<GoodsDiscountVo> querygoodsDiscount();

    int updateByPrimaryKeySelective(GoodsDiscount record);

    int updateByPrimaryKey(GoodsDiscount record);
}