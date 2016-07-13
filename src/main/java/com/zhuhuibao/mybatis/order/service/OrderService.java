package com.zhuhuibao.mybatis.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.Order;
import com.zhuhuibao.mybatis.order.mapper.OrderMapper;


/**
 * 订单处理
 */
@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    OrderMapper mapper;


    /**
     * 新增订单记录
     *
     * @param order order
     */
    public void insert(Order order) {
        int count;
        try {
            count = mapper.insertSelective(order);
            if (count != 1) {
                log.error("t_o_order:插入数据失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入数据失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入数据失败");
        }
    }

    /**
     * 根据订单号获取订单信息
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    public Order findByOrderNo(String orderNo) {
        Order order;
        try {
            order = mapper.selectByPrimaryKey(orderNo);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询数据失败");
        }
        return order;
    }

    /**
     * 根据订单号更新 订单信息
     *
     * @param order 订单信息
     */
    public boolean update(Order order) {

        int count;

        try {
            count = mapper.updateByPrimaryKeySelective(order);
            if (count  == 0) {
                log.error("t_o_order:更新数据失败");
                throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新数据失败");
            }
            log.error("update t_o_order : [count] >>>> {}",count);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "更新数据失败");
        }
        return count > 0;
    }

    /**
     * 批量修改订单状态
     * @param orderNoList 订单编号
     * @param status      状态
     */
    public void batchUpdateStatus(List<String> orderNoList, String status) {
        for (String  orderNo : orderNoList) {
            Order order = new Order();
            order.setOrderNo(orderNo);
            order.setStatus(status);
            update(order);
        }
    }

    /**
     * 根据课程ID和订单状态查询订单
     * @param courseId
     * @param status
     * @return
     */
    public List<Order> findListByCourseIdAndStatus(String courseId, String status) {

        return mapper.findListByCourseIdAndStatus(courseId,status);
    }


    /**
     * 查询购买排行top10
     * @param goodsType
     * @return
     */
    public List<Map<String, String>> findHotbuyTopten(String goodsType) {
        List<Map<String,String>> list;
        try{
            list = mapper.findHotbuyTopten(goodsType);
        } catch (Exception e){
            e.printStackTrace();
            log.error("查询失败");
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL,"查询失败");
        }
        return list;
    }
}
