package com.movision.mybatis.period.service;

import com.movision.mybatis.period.entity.Period;
import com.movision.mybatis.period.mapper.PeriodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/2/8 14:36
 */
@Service
@Transactional
public class PeriodService {
    Logger log = LoggerFactory.getLogger(PeriodService.class);
    @Autowired
    PeriodMapper periodMapper;

    /**
     * 添加活动周期
     *
     * @param map
     * @return
     */
    public int insertPostRecord(Period map) {
        try {
            log.info("添加活动周期");
            return periodMapper.insertSelectiveTwo(map);
        } catch (Exception e) {
            log.error("活动周期添加失败");
            throw e;
        }
    }
}
