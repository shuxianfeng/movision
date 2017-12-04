package com.movision.mybatis.userDauStatistics.service;

import com.movision.mybatis.userDauStatistics.entity.UserDauStatisticsVo;
import com.movision.mybatis.userDauStatistics.mapper.UserDauStatisticsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/12/4 10:56
 */
@Service
public class UserDauStatisticsService {

    private static Logger log = LoggerFactory.getLogger(UserDauStatisticsService.class);

    @Autowired
    private UserDauStatisticsMapper userDauStatisticsMapper;

    public List<UserDauStatisticsVo> queryUserStatistics(Map map) {
        try {
            log.info("查询首页用户统计列表");
            return userDauStatisticsMapper.queryUserStatistics(map);
        } catch (Exception e) {
            log.error("查询首页用户统计列表异常", e);
            throw e;
        }
    }

    public UserDauStatisticsVo queryUserStatisticsGather(Map map) {
        try {
            log.info("查询首页用户统计");
            return userDauStatisticsMapper.queryUserStatisticsGather(map);
        } catch (Exception e) {
            log.error("查询首页用户统计异常", e);
            throw e;
        }
    }
}
