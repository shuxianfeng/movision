package com.movision.facade.boss;

import com.movision.mybatis.bossIndex.entity.AboveStatistics;
import com.movision.mybatis.bossIndex.entity.IndexTodayDetails;
import com.movision.mybatis.bossIndex.entity.ProcessedGoodsOrders;
import com.movision.mybatis.bossIndex.service.IndexService;
import com.movision.mybatis.user.entity.UserParticulars;
import com.movision.mybatis.userDauStatistics.entity.UserDauStatistics;
import com.movision.mybatis.userDauStatistics.entity.UserDauStatisticsVo;
import com.movision.mybatis.userDauStatistics.service.UserDauStatisticsService;
import com.movision.mybatis.userParticipate.entity.UserParticipate;
import com.movision.mybatis.userParticipate.entity.UserParticipateVo;
import com.movision.mybatis.userParticipate.service.UserParticipateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/3/11 15:40
 */
@Service
public class IndexFacade {

    @Autowired
    private IndexService indexService;

    @Autowired
    private UserDauStatisticsService userDauStatisticeService;

    @Autowired
    private UserParticipateService userParticipateService;

    /**
     * 查询后台首页今日详情
     *
     * @return
     */
    public IndexTodayDetails queryTheHomepageDetailsToday() {
        return indexService.queryTheHomepageDetailsToday();
    }

    /**
     * 查询后台首页待处理、商品、订单
     *
     * @return
     */
    public ProcessedGoodsOrders queryProcessedGoodsOrders() {
        ProcessedGoodsOrders pgo = indexService.queryProcessedGoodsOrders();
        Integer add = pgo.getAddVExamine();
        /*Integer shop=pgo.getShopExamine();*/
        pgo.setPendingQuantity(add);//待审核，加V审批和店铺审批合
        return pgo;
    }

    /**
     * 首页上方统计查询
     *
     * @return
     */
    public AboveStatistics queryAboveStatistics() {
        return indexService.queryAboveStatistics();
    }

    public Map queryUserStatistics(String time) {
        Map resault = new HashMap();
        String[] dates = time.split(",");
        Date begin = null;
        Date end = null;
        Map map = new HashMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            begin = format.parse(dates[0]);
            end = format.parse(dates[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        map.put("begin", begin);
        map.put("end", end);
        List<UserDauStatisticsVo> list = userDauStatisticeService.queryUserStatistics(map);
        resault.put("lists", list);
        UserDauStatisticsVo vo = userDauStatisticeService.queryUserStatisticsGather(map);
        resault.put("gather", vo);
        return resault;
    }

    public Map queryPostStatistics(String time) {
        Map resault = new HashMap();
        String[] dates = time.split(",");
        Date begin = null;
        Date end = null;
        Map map = new HashMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            begin = format.parse(dates[0]);
            end = format.parse(dates[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        map.put("begin", begin);
        map.put("end", end);
        ///条件查询列表
        List<UserParticipateVo> list = userParticipateService.queryPostStatistics(map);
        //查询条件汇总
        resault.put("lists", list);
        UserParticipateVo vo = userParticipateService.queryPostStatisticsGather(map);
        resault.put("gather", vo);
        return resault;
    }
}
