package com.zhuhuibao.mybatis.order.service;

import com.google.gson.Gson;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.Invoice;
import com.zhuhuibao.mybatis.order.mapper.InvoiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 发票业务处理类
 *
 * @author pl
 * @version 2016/6/14 0014
 */
@Service
@Transactional
public class InvoiceService {

    private final static Logger log = LoggerFactory.getLogger(InvoiceService.class);

    @Autowired
    InvoiceMapper invoiceMapper;

    /**
     * 新增发票信息
     * @param json  发票内容
     */
    public void insertInvoice(String json)
    {
        int result;
        try{
            Gson gson = new Gson();
            Invoice invoice = gson.fromJson(json,Invoice.class);
            result = invoiceMapper.insert(invoice);
            if (result != 1) {
                log.error("t_o_invoice:插入数据失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入数据失败");
            }
        }catch(Exception e)
        {
            log.error("insert invoice error!",e);
            throw e;
        }
    }

    /**
     * 运营管理平台更新发票状态
     * @param invoice 发票信息
     */
    public void updateInvoice(Invoice invoice)
    {
        int result;
        try{
            result = invoiceMapper.updateByPrimaryKeySelective(invoice);
            if (result != 1) {
                log.error("t_o_invoice:更新状态失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "更新状态失败");
            }
        }catch(Exception e)
        {
            log.error("update invoice error!",e);
            throw e;
        }
    }

}
