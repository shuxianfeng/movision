package com.zhuhuibao.mybatis.tech.service;

import com.zhuhuibao.mybatis.tech.entity.TrainPwdTicket;
import com.zhuhuibao.mybatis.tech.mapper.TrainPwdTicketMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 培训密码券管理
 *
 * @author Administrator
 * @version 2016/6/6 0006
 */
@Service
@Transactional
public class TrainPwdTicketManage {

    private final static Logger log = LoggerFactory.getLogger(TrainPwdTicketManage.class);

    @Autowired
    TrainPwdTicketMapper ticketMapper;
    /**
     * 查询培训的密码券
     * @param pager  分页条件
     * @param condition 查询条件
     * @return
     */
    public List<Map<String,String>> findAllTrainPwdTicket(Paging<Map<String,String>> pager, Map<String,Object> condition)
    {
        log.info("find all oms order for pager "+ StringUtils.mapToString(condition));
        List<Map<String,String>> ticketList;
        try{
            ticketList = ticketMapper.findAllTrainPwdTicket(pager.getRowBounds(),condition);
        }catch(Exception e)
        {
            log.error("find all oms order for pager error!",e);
            throw e;
        }
        return ticketList;
    }

    /**
     * 更新培训的密码券状态
     * @param ticketId 密码券ID
     * @param status 状态
     * @return
     */
    public int updateTrainPwdTicketStatus(Long ticketId,String status)
    {
        log.info("update train pwd ticket status ticketId "+ ticketId+" status = "+status);
        int result;
        try{
            TrainPwdTicket ticket = new TrainPwdTicket();
            ticket.setTicketId(ticketId);
            ticket.setStatus(Integer.parseInt(status));
            result = ticketMapper.updateByPrimaryKeySelective(ticket);
        }catch(Exception e) {
            log.error("update train pwd ticket status info error!",e);
            throw e;
        }
        return result;
    }

    /**
     * 查看培训的密码券
     * @param ticketId 密码券ID
     * @return
     */
    public Map<String,Object> selectTrainPwdTicket(Long ticketId)
    {
        log.info("select train pwd ticket info ticketId "+ ticketId);
        Map<String,Object> ticket;
        try{
            ticket = ticketMapper.selectByPrimaryKey(ticketId);
        }catch(Exception e) {
            log.error("select train pwd ticket info error!",e);
            throw e;
        }
        return ticket;
    }
}
