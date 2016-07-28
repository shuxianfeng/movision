package com.zhuhuibao.mybatis.order.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.OrderSms;
import com.zhuhuibao.mybatis.order.mapper.OrderSmsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 退款(批量)
 */

@Service
public class OrderSmsService {
    private static final Logger log = LoggerFactory.getLogger(OrderSmsService.class);

    @Autowired
    OrderSmsMapper mapper;


    /**
     * 新增记录
     * @param record record
     */
    public void insert(OrderSms record) {
        int count;
        try {
            count = mapper.insertSelective(record);
            if (count != 1) {
                log.error("t_o_sms:插入数据失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入数据失败");
            }
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入数据失败");
        }
    }

    /**
     * 更新记录
     * @param record
     */
    public void update(OrderSms record) {
        int count;
        try {
            count = mapper.updateByPrimaryKeySelective(record);
            if (count != 1) {
                log.error("t_o_sms:更新数据失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "更新数据失败");
            }
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "更新数据失败");
        }
    }

    /**
     * 根据订单号 和 状态 查询短信列表
     * @param orderNos
     * @param status
     * @return
     */
    public List<OrderSms> findByOrderNoAndStatusCode(List<String> orderNos, String status,String templateCode) {
        List<OrderSms> smsList;
        try{
            smsList =   mapper.findByOrderNoAndStatusCode(orderNos,status,templateCode);
        } catch (Exception e){
            log.error("执行异常>>>",e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL,"查询失败");
        }
        return  smsList;
    }

}
