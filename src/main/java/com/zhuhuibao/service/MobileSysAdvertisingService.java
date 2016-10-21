package com.zhuhuibao.service;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.mybatis.advertising.service.SysAdvertisingService;

/**
 * 广告相关接口实现类
 *
 * @author liyang
 * @date 2016年10月12日
 */
@Service
@Transactional
public class MobileSysAdvertisingService {

    private static final Logger log = LoggerFactory.getLogger(MobileSysAdvertisingService.class);

    @Autowired
    private SysAdvertisingService advService;

    /**
     * 查询广告图片信息
     * 
     * @param chanType
     *            频道类型
     * @param page
     *            频道子页面
     * @param advArea
     *            广告所属的该页面的区域
     * @return
     */
    public List<SysAdvertising> queryAdvertising(String chanType, String page, String advArea) {
        return advService.findListByCondition(chanType, page, advArea);
    }

    /**
     * 查询广告信息
     *
     * @param position
     *            数组值依次是 chanType，page，advArea
     * @return
     */
    public List<SysAdvertising> queryAdvertising(String[] position) {
        if (ArrayUtils.isNotEmpty(position) && position.length == 3) {
            return advService.findListByCondition(position[0], position[1], position[2]);
        }
        return null;
    }

}
