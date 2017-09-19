package com.movision.mybatis.testintime.service;

import com.movision.mybatis.systemPush.service.SystemPushService;
import com.movision.mybatis.testintime.entity.TestIntime;
import com.movision.mybatis.testintime.mapper.TestIntimeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhanglei
 * @Date 2017/8/9 19:25
 */
@Service
public class TestIntimeService {
    private static Logger log = LoggerFactory.getLogger(TestIntimeService.class);
    @Autowired
    private TestIntimeMapper testIntimeMapper;

    public int insert(TestIntime testIntime) {
        try {
            log.info("插入成功");
            return testIntimeMapper.insert(testIntime);
        } catch (Exception e) {
            log.error("插入失败", e);
            throw e;
        }
    }
}
