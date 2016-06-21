package com.zhuhuibao.mybatis.tech.mapper;

import com.zhuhuibao.mybatis.tech.entity.OrderOms;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface OrderManagerMapper {
    Map<String,Object> selectByPrimaryKey(Map<String,Object> con);

    int updateByPrimaryKeySelective(OrderOms record);

    List<Map<String,String>> findAllOmsTechOrder(RowBounds rowBounds, Map<String,Object> condition);

    Map<String,Object> selectCashierDeskInfo(String orderNo);
}