package com.movision.mybatis.bossIndex.mapper;

import com.movision.mybatis.bossIndex.entity.IndexTodayDetails;
import com.movision.mybatis.bossIndex.entity.ProcessedGoodsOrders;

/**
 * @Author zhurui
 * @Date 2017/3/11 15:44
 */
public interface IndexMapper {

    IndexTodayDetails queryTheHomepageDetailsToday();

    ProcessedGoodsOrders queryProcessedGoodsOrders();
}
