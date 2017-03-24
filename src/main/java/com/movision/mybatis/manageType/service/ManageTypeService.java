package com.movision.mybatis.manageType.service;

import com.movision.mybatis.manageType.entity.ManageType;
import com.movision.mybatis.manageType.mapper.ManageTypeMapper;
import com.movision.mybatis.manager.service.ManagerServcie;
import com.movision.utils.pagination.model.Paging;
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
     * 查询广告类型列表
     *
     * @return
     */
    public List<ManageType> queryAdvertisementTypeList(Paging<ManageType> pager) {
        try {
            logge.info("查询广告类型列表");
            return manageTypeMapper.findAllqueryAdvertisementTypeList(pager.getRowBounds());
        } catch (Exception e) {
            logge.error("查询广告类型列表异常");
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

    /**
     * 查询最大广告位置类型
     * @return
     */
    public Integer queryAdvertisementType() {
        try {
            logge.info("查询最大广告位置类型");
            return manageTypeMapper.queryAdvertisementType();
        } catch (Exception e) {
            logge.error("查询最大广告位置类型");
            throw e;
        }
    }

    /**
     * 根据id查询广告类型详情
     *
     * @param id
     * @return
     */
    public ManageType queryAdvertisementTypeById(String id) {
        try {
            logge.info("根据id查询广告类型详情");
            return manageTypeMapper.queryAdvertisementTypeById(id);
        } catch (Exception e) {
            logge.error("根据id查询广告类型详情异常");
            throw e;
        }
    }

    /**
     * 根据广告名称模糊查询广告类型列表
     *
     * @param map
     * @return
     */
    public List<ManageType> queryAdvertisementTypeLikeName(Map map, Paging<ManageType> pager) {
        try {
            logge.info("根据广告名称模糊查询广告类型列表");
            return manageTypeMapper.findAllQueryAdvertisementTypeLikeName(map, pager.getRowBounds());
        } catch (Exception e) {
            logge.error("根据广告名称模糊查询广告类型列表异常");
            throw e;
        }
    }

    /**
     * 查询广告位置排序
     *
     * @param type
     * @return
     */
    public Integer queryAdvertisementLocation(String type) {
        try {
            logge.info("查询广告位置排序");
            return manageTypeMapper.queryAdvertisementLocation(type);
        } catch (Exception e) {
            logge.error("查询广告位置排序异常");
            throw e;
        }
    }

    /**
     * 编辑广告类型
     *
     * @param manageType
     * @return
     */
    public int updateAdvertisementType(ManageType manageType) {
        try {
            logge.info("编辑广告类型");
            return manageTypeMapper.updateByPrimaryKeySelective(manageType);
        } catch (Exception e) {
            logge.error("编辑广告类型异常");
            throw e;
        }
    }
}
