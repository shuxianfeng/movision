package com.movision.mybatis.homepageManage.service;

import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.mapper.HomepageManageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author shuxf
 * @Date 2017/1/18 14:32
 */
@Service
public class HomepageManageService {
    @Autowired
    private HomepageManageMapper homepageManageMapper;

    public HomepageManage queryMayLikeBanner() {
        return homepageManageMapper.queryMayLikeBanner();
    }
}
