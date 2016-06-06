package com.zhuhuibao.mybatis.tech.mapper;

import com.zhuhuibao.mybatis.tech.entity.OrderOms;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface OrderManagerMapper {
    OrderOms selectByPrimaryKey(String orderNo);

    int updateByPrimaryKeySelective(OrderOms record);

    List<Map<String,String>> findAllOmsTechOrder(RowBounds rowBounds, Map<String,Object> condition);
}