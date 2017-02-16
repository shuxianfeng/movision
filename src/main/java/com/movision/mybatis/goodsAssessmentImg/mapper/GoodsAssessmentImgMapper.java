package com.movision.mybatis.goodsAssessmentImg.mapper;

import com.movision.mybatis.goodsAssessmentImg.entity.GoodsAssessmentImg;

public interface GoodsAssessmentImgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsAssessmentImg record);

    int insertSelective(GoodsAssessmentImg record);

    GoodsAssessmentImg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsAssessmentImg record);

    int updateByPrimaryKey(GoodsAssessmentImg record);
}