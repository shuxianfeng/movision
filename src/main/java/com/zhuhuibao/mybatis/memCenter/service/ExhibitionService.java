package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.mybatis.memCenter.entity.Exhibition;
import com.zhuhuibao.mybatis.memCenter.entity.MeetingOrder;
import com.zhuhuibao.mybatis.memCenter.mapper.ExhibitionMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.MeetingOrderMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/5/11 0011.
 */
@Service
@Transactional
public class ExhibitionService {
    private static final Logger log = LoggerFactory.getLogger(ExhibitionService.class);

    @Autowired
    private MeetingOrderMapper meetingOrderMapper;

    @Autowired
    private ExhibitionMapper exhibitionMapper;

    /**
     * 发布会展定制
     * @param meetingOrder
     */
    public void publishMeetingOrder(MeetingOrder meetingOrder){
        meetingOrderMapper.publishMeetingOrder(meetingOrder);
    }

    /**
     * 会展定制申请处理
     * @param meetingOrder
     */
    public void updateMeetingOrderStatus(MeetingOrder meetingOrder){
        meetingOrderMapper.updateMeetingOrderStatus(meetingOrder);
    }

    /**
     * 会展定制查看
     * @param id
     */
    public MeetingOrder queryMeetingOrderInfoById(String id){
        return meetingOrderMapper.queryMeetingOrderInfoById(id);
    }

    /**
     * 会展定制申请管理
     */
    public List<MeetingOrder> findAllMeetingOrderInfo(Paging<MeetingOrder> pager,Map<String, Object> map){
        return meetingOrderMapper.findAllMeetingOrderInfo(pager.getRowBounds(),map);
    }

    /**
     * 发布会展定制
     * @param exhibition
     */
    public void publishExhibition(Exhibition exhibition){
        exhibitionMapper.publishExhibition(exhibition);
    }

    /**
     * 会展信息列表
     * @param pager,map
     */
    public List<Exhibition> findAllExhibition(Paging<Exhibition> pager,Map<String, Object> map){
        return exhibitionMapper.findAllMeetingOrderInfo(pager.getRowBounds(),map);
    }

    /**
     * 会展详情查看
     * @param id
     */
    public Exhibition queryExhibitionInfoById(String id){
        return exhibitionMapper.queryExhibitionInfoById(id);
    }
}
