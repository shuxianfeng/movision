package com.zhuhuibao.mybatis.order.service;

import com.google.gson.Gson;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.OrderConstants;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.Invoice;
import com.zhuhuibao.mybatis.order.entity.InvoiceRecord;
import com.zhuhuibao.mybatis.order.mapper.InvoiceMapper;
import com.zhuhuibao.mybatis.order.mapper.InvoiceRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
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

    @Autowired
    InvoiceRecordMapper invoiceRecordMapper;

    /**
     * 新增发票信息
     *
     * @param json 发票内容
     */
    public void insertInvoice(String json) {
        int result;
        try {
            Gson gson = new Gson();
            Invoice invoice = gson.fromJson(json, Invoice.class);
            invoice.setCreateTime(new Date());
            result = invoiceMapper.insertSelective(invoice);
            if (result != 1) {
                log.error("t_o_invoice:插入数据失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入数据失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("insert invoice error!", e);
            throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入数据失败");
        }
    }

    /**
     * 查询发票信息
     *
     * @param id 发票ID
     */
    public Invoice queryInvoiceInfo(Long id) {
        Invoice invoice;
        try {
            invoice = invoiceMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("query invoice error!", e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询数据失败");
        }
        return invoice;
    }

    /**
     * 运营管理平台更新发票状态
     *
     * @param invoice 发票信息
     */
    public void updateInvoice(Invoice invoice) {
        int result;
        try {
            result = invoiceMapper.updateByPrimaryKeySelective(invoice);
            if (result != 1) {
                log.error("t_o_invoice:更新状态失败");
                throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新状态失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("update invoice error!", e);
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新状态失败");
        }
    }

    /**
     * 新增发票信息记录
     *
     * @param record 发票内容
     */
    public void insertInvoiceRecord(InvoiceRecord record) {
        try {
            invoiceRecordMapper.insertSelective(record);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("insert invoice error!", e);
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新状态失败");
        }
    }

    /**
     * 查询最近使用的发票信息
     *
     */
    public InvoiceRecord queryRecentUseInvoiceInfo(Long createId, String isRecentUsed) {
        InvoiceRecord invoiceRecord;
        try {
            Map<String, Object> con = new HashMap<String, Object>();
            con.put("createId", createId);
            con.put("isRecentUsed", isRecentUsed);
            invoiceRecord = invoiceRecordMapper.selectByPrimaryKey(con);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("query invoice record error!", e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询状态失败");
        }
        return invoiceRecord;
    }

    /**
     * 运营管理平台更新发票状态
     *
     * @param invoiceRecord 发票信息
     */
    public void updateInvoiceRecord(InvoiceRecord invoiceRecord) {
        int result;
        try {
            invoiceRecord.setIsRecentUsed(OrderConstants.InvoiceIsRecentUsed.YES.toString());
            result = invoiceRecordMapper.updateByPrimaryKeySelective(invoiceRecord);
            if (result == 0) {
                log.error("t_o_invoice_record:更新状态失败");
                throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新状态失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("update invoice record error!", e);
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新状态失败");
        }
    }

    /**
     * 更新发票最近是否被使用
     *
     * @param invoiceRecord 发票信息
     */
    public void updateIsRecentUsed(InvoiceRecord invoiceRecord) {
        try {
            invoiceRecordMapper.updateIsRecentUsed(invoiceRecord);
            /*if (result == 0) {
                log.error("t_o_invoice_record:更新最近被使用失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "更新最近被使用失败");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            log.error("update invoice record error!", e);
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新状态失败");
        }
    }

    /**
     * 根据订单查询发票信息
     * @param orderNo
     * @return
     */
    public Invoice findByOrderNo(String orderNo) {
        Invoice invoice;
        try{

            invoice = invoiceMapper.findByOrderNo(orderNo);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询 t_o_invoice 失败");
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL,"查询失败");
        }
        return invoice;
    }
}
