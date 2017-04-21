package com.movision.mybatis.imSystemInformRead.service;

import com.movision.mybatis.imSystemInformRead.mapper.ImSystemInformReadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/4/20 19:46
 */
@Service
public class ImSystemInformReadService {

    private static Logger log = LoggerFactory.getLogger(ImSystemInformReadService.class);

    @Autowired
    private ImSystemInformReadMapper imSystemInformReadMapper;

    /**
     * 更新系统推送已读状态
     *
     * @return
     */
    public Integer updateSystemRead(Map map) {
        try {
            log.info("更新系统推送已读状态");
            return imSystemInformReadMapper.updateSystemRead(map);
        } catch (Exception e) {
            log.error("更新系统推送已读状态异常", e);
            throw e;
        }
    }

    /**
     * 查询该用户是否查看过此条系统推送
     *
     * @param map
     * @return
     */
    public Integer queryUserCheckPush(Map map) {
        try {
            log.info("查询该用户是否查看过此条系统推送");
            return imSystemInformReadMapper.queryUserCheckPush(map);
        } catch (Exception e) {
            log.error("查询该用户是否查看过此条系统推送异常", e);
            throw e;
        }
    }
}
