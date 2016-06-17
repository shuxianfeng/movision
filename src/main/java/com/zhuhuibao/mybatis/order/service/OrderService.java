package com.zhuhuibao.mybatis.order.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.Order;
import com.zhuhuibao.mybatis.order.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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
            throw e;
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
            throw e;
        }
        return order;
    }

    /**
     * 根据订单号更新 订单信息
     *
     * @param order 订单信息
     */
    public void update(Order order) {

        int count;

        try {
            count = mapper.updateByPrimaryKeySelective(order);
            if (count != 1) {
                log.error("t_o_order:更新数据失败");
                throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新数据失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 批量修改订单状态
     * @param orderNoList 订单编号
     * @param status      状态
     */
    public void batchUpdateStatus(List<String> orderNoList, String status) {
       for(String orderNo : orderNoList ){
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
}
