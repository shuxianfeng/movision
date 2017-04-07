package com.movision.mybatis.invoice.mapper;

import com.movision.mybatis.invoice.entity.Invoice;
import com.movision.mybatis.invoice.entity.InvoiceVo;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Invoice record);

    int insertSelective(Invoice record);

    Invoice selectByPrimaryKey(Integer id);

    Invoice queryInvoiceInfo(int orderid);

    InvoiceVo queryInvoice(int ordernumber);

    int updateByPrimaryKeySelective(Invoice record);

    int updateByPrimaryKey(Invoice record);
}