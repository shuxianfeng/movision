package com.zhuhuibao.mybatis.order.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.OrderFlow;
import com.zhuhuibao.mybatis.order.mapper.OrderFlowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短信子表
 */
@Service
public class OrderFlowService {
    private static final Logger log = LoggerFactory.getLogger(OrderFlowService.class);

    @Autowired
    OrderFlowMapper mapper;


    /**
     * 根据订单号查询信息
     *
     * @param orderNo
     * @return
     */
    public List<OrderFlow> findByOrderNo(String orderNo) {
        List<OrderFlow> orderFlows;
        try {
            orderFlows = mapper.findByOrderNo(orderNo);

        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询数据失败");
        }
        return orderFlows;
    }


    /**
     * 根据订单号查询信息
     *
     * @param orderNo
     * @return
     */
    public OrderFlow findUniqueOrderFlow(String orderNo) {
        OrderFlow orderFlows;
        try {
            orderFlows = mapper.findUniqueOrderFlow(orderNo);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询数据失败");
        }
        return orderFlows;
    }

    /**
     * 根据订单号和支付方式查询信息
     *
     * @param orderNo
     * @param tradeMode
     * @return
     */
    public OrderFlow findByOrderNoAndTradeMode(String orderNo, String tradeMode) {
        OrderFlow orderFlow;
        try {
            orderFlow = mapper.findByOrderNoAndTradeMode(orderNo, tradeMode);

        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询数据失败");
        }
        return orderFlow;
    }

    /**
     * 插入记录
     *
     * @param orderFlow
     */
    public void insert(OrderFlow orderFlow) {
        int count;
        try {
            count = mapper.insertSelective(orderFlow);
            if (count != 1) {
                log.error("t_o_order_flow:插入数据失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入数据失败");
            }
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "插入数据失败");
        }
    }

    /**
     * 更新记录
     *
     * @param alFlow
     */
    public void update(OrderFlow alFlow) {
        int count;
        try {
            count = mapper.update(alFlow);
//            if (count != 1) {
//                log.error("t_o_order_flow:更新数据失败");
//                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "更新数据失败");
//            }
            log.debug("t_o_order_flow 更新count>>>[{}]", count);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "更新数据失败");
        }
    }
}
