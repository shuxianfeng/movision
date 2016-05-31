package com.zhuhuibao.mybatis.tech.mapper;

import com.zhuhuibao.mybatis.tech.entity.TechData;
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

    List<Map<String,String>> findAllOMSTechDataPager(RowBounds rowBounds,Map<String,Object> condition);

    List<Map<String,String>> findAllTechDataPager(RowBounds rowBounds,Map<String,Object> condition);

    int updateTechCooperationViewsAndDownload(Long id);
}