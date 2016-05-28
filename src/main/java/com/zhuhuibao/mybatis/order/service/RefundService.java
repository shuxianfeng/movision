package com.zhuhuibao.mybatis.order.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.Refund;
import com.zhuhuibao.mybatis.order.mapper.RefundMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 退款(批量)
 */

@Service
public class RefundService {
    private static final Logger log = LoggerFactory.getLogger(RefundService.class);

    @Autowired
    RefundMapper refundMapper;


    /**
     * 新增记录
     * @param refund record
     */
    public void insert(Refund refund) {
        int count;
        try {
            count = refundMapper.insertSelective(refund);
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
}
