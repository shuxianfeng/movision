package com.movision.mybatis.deviceAccid.service;

import com.movision.mybatis.deviceAccid.entity.DeviceAccid;
import com.movision.mybatis.deviceAccid.mapper.DeviceAccidMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhuangyuhao
 * @Date 2017/4/15 13:39
 */
@Service
public class DeviceAccidService {

    private static Logger log = LoggerFactory.getLogger(DeviceAccidService.class);

    @Autowired
    private DeviceAccidMapper deviceAccidMapper;

    public Integer add(DeviceAccid deviceAccid) {
        try {
            log.info("新增设备和accid关系");
            return deviceAccidMapper.insertSelective(deviceAccid);
        } catch (Exception e) {
            log.error("新增设备和accid关系失败", e);
            throw e;
        }
    }

    public int delete(Integer id) {
        try {
            log.info("删除设备和accid关系");
            return deviceAccidMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            log.error("删除设备和accid关系失败", e);
            throw e;
        }
    }

    public DeviceAccid selectByDeviceno(String deviceno) {
        try {
            log.info("根据设备号获取设备号与accid的关系");
            return deviceAccidMapper.selectByDeviceno(deviceno);
        } catch (Exception e) {
            log.error("根据设备号获取设备号与accid的关系失败", e);
            throw e;
        }
    }

}
