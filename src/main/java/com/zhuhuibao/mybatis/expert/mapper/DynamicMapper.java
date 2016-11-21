package com.zhuhuibao.mybatis.expert.mapper;

import com.zhuhuibao.mybatis.expert.entity.Dynamic;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface DynamicMapper {

    int publishDynamic(Dynamic dynamic);

    Dynamic queryDynamicById(String id);

    int updateDynamic(Dynamic dynamic);

    List<Dynamic> findAllDynamicList(RowBounds rowBounds, Map<String, Object> map);

    List<Map<String,String>> findDynamicListByCount(int count);

    int updateDynamicViews(Dynamic dynamic);
    List<Dynamic> findAllDynamicList1(RowBounds rowBounds, Map<String, Object> map);
}