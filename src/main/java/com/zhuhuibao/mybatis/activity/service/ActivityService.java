package com.zhuhuibao.mybatis.activity.service;

import com.zhuhuibao.mybatis.activity.entity.ActivityApply;
import com.zhuhuibao.mybatis.activity.mapper.ActivityApplyMapper;
import com.zhuhuibao.mybatis.activity.mapper.ActivityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
@Service
@Transactional
public class ActivityService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    @Autowired
    private ActivityApplyMapper activityApplyMapper;

    @Autowired
    private ActivityMapper activityMapper;

    public int addActivity(ActivityApply activityApply){
        try{
            return activityApplyMapper.insertSelective(activityApply);
        }catch (Exception e){
            log.error("ActivityService::addActivity", e);
            // e.printStackTrace();
            throw e;
        }
    }
}
