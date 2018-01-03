package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.IndexFacade;
import com.movision.mybatis.bossIndex.entity.AboveStatistics;
import com.movision.mybatis.bossIndex.entity.IndexTodayDetails;
import com.movision.mybatis.bossIndex.entity.ProcessedGoodsOrders;
import com.movision.mybatis.user.entity.UserChannelStatistics;
import com.movision.mybatis.userDauStatistics.entity.UserDauStatistics;
import com.movision.mybatis.userDauStatistics.entity.UserDauStatisticsVo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/3/11 15:23
 */
@RestController
@RequestMapping(value = "/boss/index/")
public class BossIndexController {

    @Autowired
    private IndexFacade indexFacade;

    /**
     * 查询后台首页今日详情
     *
     * @return
     */
    @ApiOperation(value = "查询首页显示今日详情", notes = "用于查询首页显示今日详情接口", response = Response.class)
    @RequestMapping(value = "query_the_homepage_datails_today", method = RequestMethod.POST)
    public Response queryTheHomepageDetailsToday() {
        Response response = new Response();
        IndexTodayDetails today = indexFacade.queryTheHomepageDetailsToday();
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(today);
        return response;
    }

    /**
     * 查询后台首页待处理、商品、订单
     *
     * @return
     */
    @ApiOperation(value = "查询首页待处理统计、商品统计、订单统计", notes = "查询首页待处理统计、商品统计、订单统计", response = Response.class)
    @RequestMapping(value = "query_processed_goods_orders", method = RequestMethod.POST)
    public Response queryProcessedGoodsOrders() {
        Response response = new Response();
        ProcessedGoodsOrders resault = indexFacade.queryProcessedGoodsOrders();
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(resault);
        return response;
    }


    /**
     * 首页上方统计查询
     *
     * @return
     */
    @ApiOperation(value = "首页上方查询统计", notes = "用于首页上方统计查询接口", response = Response.class)
    @RequestMapping(value = "query_above_statistics", method = RequestMethod.POST)
    public Response queryAboveStatistics() {
        Response response = new Response();
        AboveStatistics aboveStatistics = indexFacade.queryAboveStatistics();
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(aboveStatistics);
        return response;
    }


    /**
     * 首页用户统计
     *
     * @param time
     * @return
     */
    @RequestMapping(value = "query_user_statistics", method = RequestMethod.POST)
    @ApiOperation(value = "用户统计", notes = "用户统计", response = Response.class)
    public Response queryUserStatistics(@ApiParam(value = "时间范围") @RequestParam(required = false) String time) {
        Response response = new Response();
        Map statistics = indexFacade.queryUserStatistics(time);
        response.setMessage("查询成功");
        response.setData(statistics);
        return response;
    }

    @RequestMapping(value = "query_user_channel_statistics", method = RequestMethod.POST)
    @ApiOperation(value = "统计用户渠道数量", notes = "统计用户渠道数量", response = Response.class)
    public Response queryUserChannelStatistics() {
        Response response = new Response();
        List<UserChannelStatistics> list = indexFacade.queryUserChannelStatistics();
        response.setMessage("查询成功");
        response.setData(list);
        return response;
    }

    /**
     * 查询平台统计数据
     *
     * @param time
     * @return
     */
    @RequestMapping(value = "query_post_statistics", method = RequestMethod.POST)
    @ApiOperation(value = "查询平台数据统计", notes = "查询平台数据统计", response = Response.class)
    public Response queryPostStatistics(@ApiParam(value = "时间范围") @RequestParam(required = false) String time) {
        Response response = new Response();
        Map map = indexFacade.queryPostStatistics(time);
        response.setMessage("查询成功");
        response.setData(map);
        return response;
    }
}
