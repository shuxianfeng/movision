package com.zhuhuibao.mybatis.oms.mapper;

import com.zhuhuibao.mybatis.oms.entity.ComplainSuggest;

public interface ComplainSuggestMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ComplainSuggest record);

    int insertSelective(ComplainSuggest record);

    ComplainSuggest selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ComplainSuggest record);

    int updateByPrimaryKey(ComplainSuggest record);
}