package com.zhuhuibao.mybatis.techtrain.mapper;

import com.zhuhuibao.mybatis.tech.entity.DictionaryTechData;

import java.util.List;

import com.zhuhuibao.mybatis.tech.entity.DictionaryTechData;
/**
 * 技术资料(解决方案，技术资料，培训资料)分类常量DAO层
 * @author  penglong
 * @create 2016-05-27
 */
public interface DictionaryTechDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DictionaryTechData record);

    int insertSelective(DictionaryTechData record);

    DictionaryTechData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DictionaryTechData record);

    int updateByPrimaryKey(DictionaryTechData record);

    List<DictionaryTechData> selectFirstCategory();

    DictionaryTechData selectSecondCategoryById(Integer firstCategoryId);
}