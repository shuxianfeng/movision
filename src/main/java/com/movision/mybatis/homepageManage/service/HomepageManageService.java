package com.movision.mybatis.homepageManage.service;

import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.mapper.HomepageManageMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/1/18 14:32
 */
@Service
@Transactional
public class HomepageManageService {
    private static Logger log = LoggerFactory.getLogger(HomepageManageService.class);

    @Autowired
    private HomepageManageMapper homepageManageMapper;

    public HomepageManage queryBanner(int type) {
        try {
            log.info("根据类型查询banner图");
            return homepageManageMapper.queryBanner(type);
        } catch (Exception e) {
            log.error("根据类型查询banner图失败");
            throw e;
        }
    }

    public List<HomepageManage> queryBannerList(int type) {
        try {
            log.info("根据类型查询banner图列表");
            return homepageManageMapper.queryBannerList(type);
        } catch (Exception e) {
            log.error("根据类型查询banner图列表失败");
            throw e;
        }
    }

    /**
     * 查询广告列表
     *
     * @param pager
     * @return
     */
    public List<HomepageManage> queryAdvertisementList(Paging<HomepageManage> pager) {
        try {
            log.info("查询广告列表");
            return homepageManageMapper.findAllqueryAdvertisementList(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询广告列表");
            throw e;
        }
    }
}
