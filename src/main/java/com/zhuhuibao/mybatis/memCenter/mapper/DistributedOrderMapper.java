package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.DistributedOrder;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface DistributedOrderMapper {

    int publishDistributedOrder(DistributedOrder distributedOrder);

    DistributedOrder queryDistributedOrderInfoById(String id);

    int updateDistributedStatus(DistributedOrder distributedOrder);

    List<Map<String,String>> findAllDistributedOrder(RowBounds rowBounds,Map<String,Object> map);
}