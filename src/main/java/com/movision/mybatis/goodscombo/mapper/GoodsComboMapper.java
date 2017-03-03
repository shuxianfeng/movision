package com.movision.mybatis.goodscombo.mapper;

import com.movision.mybatis.goodscombo.entity.GoodsCombo;

public interface GoodsComboMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCombo record);

    int insertSelective(GoodsCombo record);

    GoodsCombo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsCombo record);

    int updateByPrimaryKey(GoodsCombo record);
}