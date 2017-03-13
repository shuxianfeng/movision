package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.IndexFacade;
import com.movision.mybatis.bossIndex.entity.IndexTodayDetails;
import com.movision.mybatis.bossIndex.entity.ProcessedGoodsOrders;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
