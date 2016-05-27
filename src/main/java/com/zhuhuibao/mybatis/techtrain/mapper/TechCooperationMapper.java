package com.zhuhuibao.mybatis.techtrain.mapper;

import com.zhuhuibao.mybatis.techtrain.entity.TechCooperation;

/**
 * 技术合作：技术成果，技术需求 DAO层
 * @author  penglong
 * @create 2016-05-27
 */
public interface TechCooperationMapper {

    int insertSelective(TechCooperation record);

    TechCooperation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TechCooperation record);

}