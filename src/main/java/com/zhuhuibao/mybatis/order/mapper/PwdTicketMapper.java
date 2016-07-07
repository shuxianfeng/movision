package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.PwdTicket;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PwdTicketMapper {
    int deleteByPrimaryKey(Long ticketId);

    int insert(PwdTicket record);

    int insertSelective(PwdTicket record);

    PwdTicket selectByPrimaryKey(Long ticketId);

    int updateByPrimaryKeySelective(PwdTicket record);

    int updateByPrimaryKey(PwdTicket record);

    List<PwdTicket> findByCourseId(@Param("courseid") Long courseid);

    void updateStatusByCourseId(@Param("courseId") Long courseId, @Param("status") String status,
                                @Param("operateId") Integer operateId);

    List<PwdTicket> findByOrderNo(@Param("orderNo") String orderNo);
}