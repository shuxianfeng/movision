package com.movision.mybatis.systemPush.service;

import com.movision.mybatis.systemPush.mapper.SystemPushMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author zhanglei
 * @Date 2017/3/16 16:30
 */
@Service
@Transactional
public class SystemPushService {
    private static Logger log = LoggerFactory.getLogger(SystemPushService.class);

    @Autowired
    private SystemPushMapper systemPushMapper;

}
