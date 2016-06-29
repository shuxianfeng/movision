package com.zhuhuibao.mybatis.expo.mapper;

import com.zhuhuibao.mybatis.expo.entity.Exhibition;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface ExhibitionMapper {
    int publishExhibition(Exhibition exhibition);

    Exhibition queryExhibitionInfoById(String id);

    int updateExhibitionInfoById(Exhibition record);

    int updateExhibitionViews(Exhibition record);

    List<Map<String,String>> findAllExhibition(RowBounds rowBounds, Map<String, Object> map);

    List<Map<String,String>> findNewExhibition(Map<String, Object> map);

    List<Map<String,String>> queryHomepageExhibitionList(Map<String, Object> map);

    int queryMyExhibitionListSize(Map<String, Object> map);

}