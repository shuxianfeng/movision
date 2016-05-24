package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Exhibition;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface ExhibitionMapper {
    int publishExhibition(Exhibition exhibition);

    Exhibition queryExhibitionInfoById(String id);

    int updateExhibitionInfoById(Exhibition record);

    List<Exhibition> findAllExhibition(RowBounds rowBounds,Map<String, Object> map);

    List<Exhibition> findNewExhibition(Map<String,Object> map);

}