package com.movision.mybatis.combo.service;

import com.movision.mybatis.combo.mapper.ComboMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author shuxf
 * @Date 2017/3/3 10:39
 */
@Service
public class ComboService {

    @Autowired
    private ComboMapper comboMapper;

    private Logger log = LoggerFactory.getLogger(ComboService.class);

    public double queryComboPrice(int comboid) {
        try {
            log.info("根据套餐id查询套餐折后价");
            return comboMapper.queryComboPrice(comboid);
        } catch (Exception e) {
            log.error("根据套餐id查询套餐折后价失败");
            throw e;
        }
    }
}
