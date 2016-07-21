package com.zhuhuibao.mybatis.common.mapper;

import com.zhuhuibao.mybatis.common.entity.Suggest;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface SuggestMapper {

    int insertSelective(Suggest record);

    Map<String,String> querySuggestById(String id);

    int updateSuggest(Map<String, Object> map);

    List<Map<String,String>> findAllSuggest(RowBounds rowBounds, Map<String,Object> map);
}