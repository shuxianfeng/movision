package com.zhuhuibao.mybatis.order.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.PwdTicket;
import com.zhuhuibao.mybatis.order.mapper.PwdTicketMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * t_o_pwdticker
 * @author jianglz
 * @since 16/5/28.
 */
@Service
public class PwdTickerService {
    private static final Logger log = LoggerFactory.getLogger(PwdTickerService.class);

    @Autowired
    PwdTicketMapper mapper;

    /**
     * 新增记录
     * @param record record
     */
    public void insert(PwdTicket record) {
        int count;
        try {
            count = mapper.insertSelective(record);
            if (count != 1) {
                log.error("t_o_pwdticket:插入数据失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入数据失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 批量插入
     * @param list
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void batchInsert(List<PwdTicket> list) {

        for(PwdTicket record : list){
            insert(record);
        }
    }
}
