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
 * 会展业务处理
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
    public void publishMeetingOrder(MeetingOrder meetingOrder)throws Exception{
        try {
            meetingOrderMapper.publishMeetingOrder(meetingOrder);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 会展定制申请处理
     * @param meetingOrder
     */
    public void updateMeetingOrderStatus(MeetingOrder meetingOrder)throws Exception{
        try {
            meetingOrderMapper.updateMeetingOrderStatus(meetingOrder);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 会展定制查看
     * @param id
     */
    public MeetingOrder queryMeetingOrderInfoById(String id)throws Exception{
        try {
            return meetingOrderMapper.queryMeetingOrderInfoById(id);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 会展定制申请管理
     */
    public List<MeetingOrder> findAllMeetingOrderInfo(Paging<MeetingOrder> pager,Map<String, Object> map)throws Exception{
        try {
            return meetingOrderMapper.findAllMeetingOrderInfo(pager.getRowBounds(),map);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 发布会展定制
     * @param exhibition
     */
    public void publishExhibition(Exhibition exhibition)throws Exception{
        try {
            exhibitionMapper.publishExhibition(exhibition);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 会展信息列表
     * @param pager,map
     */
    public List<Exhibition> findAllExhibition(Paging<Exhibition> pager,Map<String, Object> map)throws Exception{
        try {
            return exhibitionMapper.findAllExhibition(pager.getRowBounds(),map);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 会展详情查看
     * @param id
     */
    public Exhibition queryExhibitionInfoById(String id)throws Exception{
        try {
            return exhibitionMapper.queryExhibitionInfoById(id);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 会展信息编辑更新
     * @param exhibition
     */
    public void updateExhibitionInfoById(Exhibition exhibition)throws Exception{
        try {
            exhibitionMapper.updateExhibitionInfoById(exhibition);
        }catch (Exception e){
            throw e;
        }
    }
}
