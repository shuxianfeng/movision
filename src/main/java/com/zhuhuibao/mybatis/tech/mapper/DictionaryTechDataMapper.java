package com.zhuhuibao.mybatis.tech.mapper;

import com.zhuhuibao.mybatis.tech.entity.DictionaryTechData;

import java.util.List;
import java.util.Map;

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

    List<DictionaryTechData> selectSecondCategoryById(Integer firstCategoryId);

    List<Map<String,Object>> selectCategoryInfo(Integer firstCategoryId);
}