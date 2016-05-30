package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.PwdTicket;

public interface PwdTicketMapper {
    int deleteByPrimaryKey(Long ticketId);

    int insert(PwdTicket record);

    int insertSelective(PwdTicket record);

    PwdTicket selectByPrimaryKey(Long ticketId);

    int updateByPrimaryKeySelective(PwdTicket record);

    int updateByPrimaryKey(PwdTicket record);
}