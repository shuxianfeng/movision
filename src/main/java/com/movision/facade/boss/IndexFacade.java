package com.movision.facade.boss;

import com.movision.mybatis.bossIndex.entity.IndexTodayDetails;
import com.movision.mybatis.bossIndex.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhurui
 * @Date 2017/3/11 15:40
 */
@Service
public class IndexFacade {

    @Autowired
    private IndexService indexService;

    /**
     * 查询后台首页今日详情
     *
     * @return
     */
    public IndexTodayDetails queryTheHomepageDetailsToday() {
        return indexService.queryTheHomepageDetailsToday();
    }
}
