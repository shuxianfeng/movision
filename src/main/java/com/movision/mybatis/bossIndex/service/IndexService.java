package com.movision.mybatis.bossIndex.service;

import com.movision.mybatis.bossIndex.entity.IndexTodayDetails;
import com.movision.mybatis.bossIndex.entity.ProcessedGoodsOrders;
import com.movision.mybatis.bossIndex.mapper.IndexMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhurui
 * @Date 2017/3/11 15:42
 */
@Service
public class IndexService {
    private static Logger logger = LoggerFactory.getLogger(IndexService.class);

    @Autowired
    private IndexMapper indexMapper;

    /**
     * 查询后台首页今日详情
     *
     * @return
     */
    public IndexTodayDetails queryTheHomepageDetailsToday() {
        try {
            logger.info("查询后台首页今日详情");
            return indexMapper.queryTheHomepageDetailsToday();
        } catch (Exception e) {
            logger.error("查询后台首页今日详情异常");
            throw e;
        }
    }

    /**
     * 查询后台首页待处理、商品、订单
     *
     * @return
     */
    public ProcessedGoodsOrders queryProcessedGoodsOrders() {
        try {
            logger.info("查询后台首页待处理、商品、订单");
            return indexMapper.queryProcessedGoodsOrders();
        } catch (Exception e) {
            logger.error("查询后台首页待处理、商品、订单异常");
            throw e;
        }
    }
}
