package com.zhuhuibao.mybatis.expo.service;

import com.zhuhuibao.mybatis.expo.entity.DistributedOrder;
import com.zhuhuibao.mybatis.expo.entity.Exhibition;
import com.zhuhuibao.mybatis.expo.entity.MeetingOrder;
import com.zhuhuibao.mybatis.expo.mapper.DistributedOrderMapper;
import com.zhuhuibao.mybatis.expo.mapper.ExhibitionMapper;
import com.zhuhuibao.mybatis.expo.mapper.MeetingOrderMapper;
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
public class ExpoService {
    private static final Logger log = LoggerFactory.getLogger(ExpoService.class);

    @Autowired
    private MeetingOrderMapper meetingOrderMapper;

    @Autowired
    private ExhibitionMapper exhibitionMapper;

    @Autowired
    private DistributedOrderMapper distributedOrderMapper;

    /**
     * 发布一站式会展定制
     * @param meetingOrder
     */
    public String publishMeetingOrder(MeetingOrder meetingOrder){
        try {
            meetingOrderMapper.publishMeetingOrder(meetingOrder);
            String id = meetingOrder.getId();
            return id;
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 一站式会展定制申请处理
     * @param meetingOrder
     */
    public void updateMeetingOrderStatus(MeetingOrder meetingOrder){
        try {
            meetingOrderMapper.updateMeetingOrderStatus(meetingOrder);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 一站式会展定制查看
     * @param id
     */
    public MeetingOrder queryMeetingOrderInfoById(String id){
        try {
            return meetingOrderMapper.queryMeetingOrderInfoById(id);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 一站式会展定制申请管理
     */
    public List<MeetingOrder> findAllMeetingOrderInfo(Paging<MeetingOrder> pager, Map<String, Object> map){
        try {
            return meetingOrderMapper.findAllMeetingOrderInfo(pager.getRowBounds(),map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 发布会展信息
     * @param exhibition
     */
    public void publishExhibition(Exhibition exhibition){
        try {
            exhibitionMapper.publishExhibition(exhibition);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 会展信息列表
     * @param pager,map
     */
    public List<Map<String,String>> findAllExhibition(Paging<Map<String,String>> pager,Map<String, Object> map){
        try {
            return exhibitionMapper.findAllExhibition(pager.getRowBounds(),map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 会展详情查看
     * @param id
     */
    public Exhibition queryExhibitionInfoById(String id){
        try {
            return exhibitionMapper.queryExhibitionInfoById(id);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 会展信息编辑更新
     * @param exhibition
     */
    public void updateExhibitionInfoById(Exhibition exhibition){
        try {
            exhibitionMapper.updateExhibitionInfoById(exhibition);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 会展信息点击率更新
     * @param exhibition
     */
    public void updateExhibitionViews(Exhibition exhibition){
        try {
            exhibitionMapper.updateExhibitionViews(exhibition);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 最新会展信息
     * @param map
     */
    public List<Map<String,String>> findNewExhibition(Map<String,Object> map){
        try {
            return exhibitionMapper.findNewExhibition(map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 发布分布式会展定制
     * @param distributedOrder
     */
    public int publishDistributedOrder(DistributedOrder distributedOrder){
        try {
            return distributedOrderMapper.publishDistributedOrder(distributedOrder);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 分布式会展定制列表(运营)
     * @param pager,map
     */
    public List<Map<String,String>> findAllDistributedOrder(Paging<Map<String,String>> pager,Map<String,Object> map){
        try {
            return distributedOrderMapper.findAllDistributedOrder(pager.getRowBounds(),map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 分布式会展定制查看
     * @param id
     */
    public DistributedOrder queryDistributedOrderInfoById(String id){
        try {
            return distributedOrderMapper.queryDistributedOrderInfoById(id);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 分布式会展定制申请处理
     * @param distributedOrder
     */
    public void updateDistributedStatus(DistributedOrder distributedOrder){
        try {
            distributedOrderMapper.updateDistributedStatus(distributedOrder);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<Map<String,String>> queryHomepageExhibitionList(Map<String, Object> map){
        try{
            return exhibitionMapper.queryHomepageExhibitionList(map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
