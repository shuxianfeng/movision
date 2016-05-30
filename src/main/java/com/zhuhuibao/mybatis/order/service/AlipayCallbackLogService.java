package com.zhuhuibao.mybatis.order.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.AlipayCallbackLog;
import com.zhuhuibao.mybatis.order.mapper.AlipayCallbackLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 支付宝回调 交易流水日志
 */
@Service
public class AlipayCallbackLogService {
    private static final Logger log = LoggerFactory.getLogger(AlipayCallbackLogService.class);

    @Autowired
    AlipayCallbackLogMapper mapper;


    /**
     * 新增交易流水记录
     * @param record  log
     */
    public void insert(AlipayCallbackLog record) {
        int num;
        try {
            num = mapper.insertSelective(record);
            if (num != 1) {
                log.error("t_o_alipay_callback_log:插入数据失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入数据失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }
}
