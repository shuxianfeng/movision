package com.zhuhuibao.mybatis.expo.mapper;

import com.zhuhuibao.mybatis.expo.entity.MeetingOrder;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface MeetingOrderMapper {
    int publishMeetingOrder(MeetingOrder record);

    MeetingOrder queryMeetingOrderInfoById(String id);

    int updateMeetingOrderStatus(MeetingOrder record);

    List<MeetingOrder> findAllMeetingOrderInfo(RowBounds rowBounds, Map<String, Object> map);

}