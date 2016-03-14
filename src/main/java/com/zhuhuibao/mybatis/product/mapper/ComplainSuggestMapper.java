package com.zhuhuibao.mybatis.product.mapper;

import com.zhuhuibao.mybatis.product.entity.ComplainSuggest;

public interface ComplainSuggestMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ComplainSuggest record);

    int insertSelective(ComplainSuggest record);

    ComplainSuggest selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ComplainSuggest record);

    int updateByPrimaryKey(ComplainSuggest record);
}