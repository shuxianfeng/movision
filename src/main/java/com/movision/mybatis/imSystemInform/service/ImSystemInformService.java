package com.movision.mybatis.imSystemInform.service;

import com.movision.mybatis.imSystemInform.entity.ImSystemInform;
import com.movision.mybatis.imSystemInform.mapper.ImSystemInformMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/9 20:16
 */
@Service
@Transactional
public class ImSystemInformService {
    private static Logger log = LoggerFactory.getLogger(ImSystemInformService.class);

    @Autowired
    private ImSystemInformMapper imSystemInformMapper;

    public int add(ImSystemInform imSystemInform) {
        try {
            log.info("添加系统消息");
            return imSystemInformMapper.insertSelective(imSystemInform);
        } catch (Exception e) {
            log.error("添加系统消息失败", e);
            throw e;
        }
    }
}
