package com.movision.mybatis.systemLayout.service;

import com.movision.mybatis.systemLayout.mapper.SystemLayoutMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhurui
 * @Date 2017/10/9 13:50
 */
@Service
public class SystemLayoutService {

    private static Logger log = LoggerFactory.getLogger(SystemLayoutService.class);

    @Autowired
    private SystemLayoutMapper systemLayoutMapper;

    /**
     * 查询机器人分隔数量
     *
     * @param separate
     * @return
     */
    public Integer queryRobotSeparate(String separate) {
        try {
            log.info("查询机器人分隔数量");
            return systemLayoutMapper.queryRobotSeparate(separate);
        } catch (Exception e) {
            log.error("查询机器人分隔数量异常", e);
            throw e;
        }
    }

    /**
     * 查询评论占比率
     *
     * @param percentage
     * @return
     */
    public Double queryRobotPercentage(String percentage) {
        try {
            log.info("查询评论占比率");
            return systemLayoutMapper.queryRobotpercentage(percentage);
        } catch (Exception e) {
            log.error("查询评论占比率异常", e);
            throw e;
        }
    }
}
