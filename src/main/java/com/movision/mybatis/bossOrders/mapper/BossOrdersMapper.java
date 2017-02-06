package com.movision.mybatis.bossOrders.mapper;

import com.movision.mybatis.bossOrders.entity.BossOrders;
import com.movision.mybatis.bossOrders.entity.BossOrdersVo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface BossOrdersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BossOrders record);

    int insertSelective(BossOrders record);

    BossOrders selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BossOrders record);

    int updateByPrimaryKey(BossOrders record);

    List<BossOrdersVo> queryOrdersByList(RowBounds rowBounds);

    List<BossOrdersVo> queryOrderByCondition(Map map);

    List<BossOrdersVo> queryAccuracyConditionByOrder(Map map);

    BossOrdersVo queryOrderParticulars(Integer ordernumber);
}