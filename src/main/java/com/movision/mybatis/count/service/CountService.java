package com.movision.mybatis.count.service;

import com.movision.mybatis.constant.service.ConstantService;
import com.movision.mybatis.count.mapper.CountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhanglei
 * @Date 2017/8/23 10:32
 */
@Service
public class CountService {
    static private Logger log = LoggerFactory.getLogger(CountService.class);
    @Autowired
    private CountMapper countMapper;

    public int updateTakeCount(int id) {
        try {
            log.info("修改参与次数");
            return countMapper.updateTakeCount(id);
        } catch (Exception e) {
            log.error("修改参与次数失败", e);
            throw e;
        }
    }

    public int updateAccessCount(int id) {
        try {
            log.info("修改访问次数");
            return countMapper.updateAccessCount(id);
        } catch (Exception e) {
            log.error("修改访问次数失败", e);
            throw e;
        }
    }

}
