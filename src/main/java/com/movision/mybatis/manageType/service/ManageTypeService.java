package com.movision.mybatis.manageType.service;

import com.movision.mybatis.manageType.entity.ManageType;
import com.movision.mybatis.manageType.mapper.ManageTypeMapper;
import com.movision.mybatis.manager.service.ManagerServcie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/3/6 14:21
 */
@Service
public class ManageTypeService {
    Logger logge = LoggerFactory.getLogger(ManagerServcie.class);

    @Autowired
    private ManageTypeMapper manageTypeMapper;

    /**
     * 查询广告类型
     *
     * @return
     */
    public List<ManageType> queryAdvertisementTypeList() {
        try {
            logge.info("查询广告类型");
            return manageTypeMapper.queryAdvertisementTypeList();
        } catch (Exception e) {
            logge.error("查询广告类型异常");
            throw e;
        }
    }

    /**
     * 添加广告类型
     *
     * @param map
     * @return
     */
    public int addAdvertisementType(Map map) {
        try {
            logge.info("添加广告类型");
            return manageTypeMapper.addAdvertisementType(map);
        } catch (Exception e) {
            logge.error("添加广告类型异常");
            throw e;
        }
    }
}
