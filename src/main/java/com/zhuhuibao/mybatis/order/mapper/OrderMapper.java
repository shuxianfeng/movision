package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(String orderNo);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(String orderNo);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<Order> findListByCourseIdAndStatus(@Param("courseId") String courseId, @Param("status") String status);
}