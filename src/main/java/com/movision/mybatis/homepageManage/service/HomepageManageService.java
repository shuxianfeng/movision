package com.movision.mybatis.homepageManage.service;

import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.mapper.HomepageManageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/1/18 14:32
 */
@Service
public class HomepageManageService {
    @Autowired
    private HomepageManageMapper homepageManageMapper;

    public HomepageManage queryBanner(int type) {
        return homepageManageMapper.queryBanner(type);
    }

    public List<HomepageManage> queryBannerList(int type) {
        return homepageManageMapper.queryBannerList(type);
    }
}
