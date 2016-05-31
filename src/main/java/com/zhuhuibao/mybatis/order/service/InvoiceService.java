package com.zhuhuibao.mybatis.order.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.Invoice;
import com.zhuhuibao.mybatis.order.mapper.InvoiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单发票信息
 */
@Service
public class InvoiceService {
    private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    @Autowired
    InvoiceMapper mapper;


    /**
     * 新增记录
     * @param invoice   发票信息
     */
    public void insert(Invoice invoice) {
        int count;
        try {
            count = mapper.insertSelective(invoice);
            if (count != 1) {
                log.error("t_o_invoice:插入数据失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入数据失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }
}
