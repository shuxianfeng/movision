package com.movision.mybatis.accessLog.service;

import com.movision.mybatis.accessLog.entity.AccessLog;
import com.movision.mybatis.accessLog.mapper.AccessLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author zhanglei
 * @Date 2017/10/17 14:52
 */
@Service
@Transactional

public class AccessLogService {
    @Autowired
    private AccessLogMapper accessLogMapper;

    private static Logger LOGGER = LoggerFactory.getLogger(AccessLogService.class);


    /**
     * 插入日志
     *
     * @param accessLog
     * @return
     */
    public int insertSelective(AccessLog accessLog) {
        try {
            LOGGER.info("插入日志");
            return accessLogMapper.insertSelective(accessLog);
        } catch (Exception e) {
            LOGGER.error("插入日志异常");
            throw e;
        }
    }

}
