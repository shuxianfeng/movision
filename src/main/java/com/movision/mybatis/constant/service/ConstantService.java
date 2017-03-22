package com.movision.mybatis.constant.service;

import com.movision.mybatis.constant.entity.Constant;
import com.movision.mybatis.constant.mapper.ConstantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/3/22 11:37
 */
@Service
public class ConstantService {
    static private Logger log = LoggerFactory.getLogger(ConstantService.class);
    @Autowired
    ConstantMapper constantMapper;

    public List<Constant> queryRewordList() {
        try {
            log.info("查询打赏积分数值列表");
            return constantMapper.queryRewordList();
        } catch (Exception e) {
            log.error("查询打赏积分数值列表异常");
            throw e;
        }
    }
}
