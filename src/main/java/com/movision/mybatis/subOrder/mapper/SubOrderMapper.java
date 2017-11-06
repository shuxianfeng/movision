package com.movision.mybatis.subOrder.mapper;

import com.movision.mybatis.subOrder.entity.SubOrder;
import com.movision.mybatis.subOrder.entity.SubOrderVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SubOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SubOrder record);

    void updateRentDate(Map<String, Object> parammap);

    int insertSelective(SubOrder record);

    SubOrder selectByPrimaryKey(Integer id);

    List<SubOrder> queryAllSubOrderList(int id);

    List<SubOrderVo> querySubOrderListById(int orderid);

    SubOrderVo querySubOrderInfo(Map<String, Object> parammap);

    int updateByPrimaryKeySelective(SubOrder record);

    int updateByPrimaryKey(SubOrder record);
}