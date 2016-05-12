package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.MeetingOrder;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface MeetingOrderMapper {
    int publishMeetingOrder(MeetingOrder record);

    MeetingOrder queryMeetingOrderInfoById(String id);

    int updateMeetingOrderStatus(MeetingOrder record);

    List<MeetingOrder> findAllMeetingOrderInfo(RowBounds rowBounds,Map<String, Object> map);

}