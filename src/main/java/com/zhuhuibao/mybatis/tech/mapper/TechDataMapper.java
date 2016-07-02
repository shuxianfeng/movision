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

    Map<String,String> selectMCTechDataDetail(Long id);

    int updateByPrimaryKeySelective(TechData record);

    List<Map<String,String>> findAllOMSTechDataPager(RowBounds rowBounds,Map<String,Object> condition);

    List<TechData> findAllTechDataPager(RowBounds rowBounds,Map<String,Object> condition);

    List<Map<String,String>> findScategory(Map<String,Object> condition);

    int updateTechDataViewsOrDL(Map<String,Object> map);

    List<Map<String,String>> previewTechDataDetail(Long id);

    List<Map<String,String>> findViewsOrder(Map<String,Object> condition);

    List<Map<String,String>> findDownloadOrder(Map<String,Object> condition);

    List<Map<String,String>> findIndexTechData(Map<String,Object> condition);

    Map<String,String> findTechSiteInfo(Long createId);

    Map<String,String> findTechCount();

    List<Map<String,Object>> queryDataUploadDownloadCount(Long createId);

    List<Map<String,String>> findAllTechDataOnlyPager(RowBounds rowBounds, Map<String, Object> condition);
}