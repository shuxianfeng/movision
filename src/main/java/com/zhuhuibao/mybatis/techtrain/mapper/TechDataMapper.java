package com.zhuhuibao.mybatis.techtrain.mapper;

import com.zhuhuibao.mybatis.techtrain.entity.TechData;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 技术资料
 * @author  penglong
 * @create 2016-05-27
 */
public interface TechDataMapper {

    int deleteByPrimaryKey(Map<String,Object> map);

    int insertSelective(TechData record);

    TechData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TechData record);

    List<Map<String,String>> findAllTechCooperationPager(RowBounds rowBounds, Map<String, Object> condition);

    List<Map<String,String>> findAllOMSTechCooperationPager(RowBounds rowBounds,Map<String,Object> condition);

    int updateTechCooperationViewsAndDownload(Long id);
}