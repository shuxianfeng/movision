package com.movision.mybatis.userParticipate.service;

import com.movision.mybatis.userParticipate.entity.UserParticipateVo;
import com.movision.mybatis.userParticipate.mapper.UserParticipateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/12/4 13:50
 */
@Service
public class UserParticipateService {

    private static Logger log = LoggerFactory.getLogger(UserParticipateService.class);

    @Autowired
    private UserParticipateMapper userParticipateMapper;

    public List<UserParticipateVo> queryPostStatistics(Map map) {
        try {
            log.info("查询首页用户统计列表");
            return userParticipateMapper.queryPostStatistics(map);
        } catch (Exception e) {
            log.error("查询首页用户统计列表异常", e);
            throw e;
        }
    }

    public UserParticipateVo queryPostStatisticsGather(Map map) {
        try {
            log.info("查询首页用户统计列表");
            return userParticipateMapper.queryPostStatisticsGather(map);
        } catch (Exception e) {
            log.error("查询首页用户统计列表异常", e);
            throw e;
        }
    }
}
