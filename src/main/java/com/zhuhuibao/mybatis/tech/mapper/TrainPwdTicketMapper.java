package com.zhuhuibao.mybatis.tech.mapper;

import com.zhuhuibao.mybatis.tech.entity.TrainPwdTicket;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface TrainPwdTicketMapper {
    Map<String,Object> selectByPrimaryKey(Long ticketId);

    int updateByPrimaryKeySelective(TrainPwdTicket record);

    List<Map<String,String>> findAllTrainPwdTicket(RowBounds rowBounds, Map<String,Object> condition);
}