package com.movision.mybatis.subOrder.mapper;

import com.movision.mybatis.subOrder.entity.SubOrder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SubOrder record);

    int insertSelective(SubOrder record);

    void batchInsertOrders(List<SubOrder> subOrderList);

    SubOrder selectByPrimaryKey(Integer id);

    List<SubOrder> queryAllSubOrderList(int[] ids);

    int updateByPrimaryKeySelective(SubOrder record);

    int updateByPrimaryKey(SubOrder record);
}