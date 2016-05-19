package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Dynamic;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface DynamicMapper {

    int publishDynamic(Dynamic dynamic);

    Dynamic queryDynamicById(String id);

    int updateDynamic(Dynamic dynamic);

    List<Dynamic> findAllDynamicList(RowBounds rowBounds, Map<String,Object> map);

    List<Map<String,String>> findDynamicListByCount(int count);

}