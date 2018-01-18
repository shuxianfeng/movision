package com.movision.mybatis.accessLog.service;

import com.movision.mybatis.accessLog.entity.AccessLog;
import com.movision.mybatis.accessLog.entity.AccessLogVo;
import com.movision.mybatis.accessLog.mapper.AccessLogMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 查询平台访问日志列表
     *
     * @param accessLog
     * @param pager
     * @return
     */
    public List<AccessLogVo> findAllqueryPlatformAccess(AccessLog accessLog, Paging<AccessLogVo> pager) {
        try {
            LOGGER.info("查询平台访问日志列表");
            return accessLogMapper.findAllqueryPlatformAccess(accessLog, pager.getRowBounds());
        } catch (Exception e) {
            LOGGER.error("查询平台访问日志列表异常", e);
            throw e;
        }
    }

}
