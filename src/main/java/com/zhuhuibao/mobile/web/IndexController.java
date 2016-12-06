package com.zhuhuibao.mobile.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhuhuibao.mybatis.news.form.NewsForm;
import com.zhuhuibao.service.MobileExpertPageService;
import com.zhuhuibao.service.NewsService;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.AdvertisingConstant;
import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.service.MobileSysAdvertisingService;

/**
 * 移动端首页
 *
 * Created by tongxinglong on 2016/10/11 0011.
 */
@RestController
@RequestMapping("/rest/m/index")
public class IndexController {
    @Autowired
    private MobileSysAdvertisingService advertisingService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private MobileExpertPageService mobileExpertPageService;

    @ApiOperation(value = "触屏端首页", notes = "触屏端工程商首页")
    @RequestMapping(value = "/site", method = RequestMethod.GET)
    public Response index() {
        // banner位广告图片
        List<SysAdvertising> banner = advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingPosition.M_Homepage_Banner.value);
        // 头条
        List<SysAdvertising> headline = advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingPosition.M_Homepage_Headline.value);
        // 频道推广
        List<SysAdvertising> marketing = advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingPosition.M_Homepage_Marketing.value);
        // 盟友邀请
        List<SysAdvertising> invitation = advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingPosition.M_Homepage_Invitation.value);
        // 资讯
        Paging<NewsForm> newsPager = new Paging<>(1, 3);
        List<NewsForm> newsList = newsService.mobileSelNewsList("1", null, null, newsPager);

        //协会动态区域
        List<Map<String, String>> dynamicList = mobileExpertPageService.findNewDynamicList(3);

        Map<String, List> advMap = new HashMap<>();
        advMap.put("banner", banner);
        advMap.put("headline", headline);
        advMap.put("marketing", marketing);
        advMap.put("invitation", invitation);
        advMap.put("newsList", newsList);
        advMap.put("dynamicList", dynamicList);
        return new Response(advMap);
    }
}
