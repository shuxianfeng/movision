package com.movision.facade.index;

import com.movision.mybatis.circleCategory.entity.CircleCategory;
import com.movision.mybatis.circleCategory.service.CircleCategoryService;
import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.service.HomepageManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/18 17:51
 */
@Service
public class FacadeDiscover {

    @Autowired
    private HomepageManageService homepageManageService;

    @Autowired
    private CircleCategoryService circleCategoryService;

    public Map<String, Object> queryDiscoverIndexData() {

        HashMap<String, Object> map = new HashMap();
        List<HomepageManage> homepageManageList = homepageManageService.queryDiscoverBanner();//查询发现页顶部banner轮播图
        List<CircleCategory> circleCategoryList = circleCategoryService.queryCircleCategoryList();//查询发现页次banner所有圈子类别轮播图

        map.put("homepageManageList", homepageManageList);
        map.put("circleCategoryList", circleCategoryList);
        return map;
    }

}
