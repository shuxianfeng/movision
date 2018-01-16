package com.movision.facade.boss;

import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.accessLog.entity.AccessLog;
import com.movision.mybatis.accessLog.service.AccessLogService;
import com.movision.mybatis.bossIndex.entity.AboveStatistics;
import com.movision.mybatis.bossIndex.entity.IndexTodayDetails;
import com.movision.mybatis.bossIndex.entity.ProcessedGoodsOrders;
import com.movision.mybatis.bossIndex.service.IndexService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.UserChannelStatistics;
import com.movision.mybatis.user.entity.UserParticulars;
import com.movision.mybatis.user.service.UserService;
import com.movision.mybatis.userDauStatistics.entity.UserDauStatistics;
import com.movision.mybatis.userDauStatistics.entity.UserDauStatisticsVo;
import com.movision.mybatis.userDauStatistics.service.UserDauStatisticsService;
import com.movision.mybatis.userParticipate.entity.UserParticipate;
import com.movision.mybatis.userParticipate.entity.UserParticipateVo;
import com.movision.mybatis.userParticipate.service.UserParticipateService;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private UserService userService;

    @Autowired
    private AccessLogService accessLogService;

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

    /**
     * 首页用户统计
     *
     * @param time
     * @return
     */
    public Map queryUserStatistics(String time) {
        Map resault = new HashMap();
        Date begin = null;
        Date end = null;
        Map map = new HashMap();
        if (StringUtil.isNotEmpty(time)) {
            String[] dates = time.split(",");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                begin = format.parse(dates[0]);
                end = format.parse(dates[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("begin", begin);
        map.put("end", end);
        List<UserDauStatisticsVo> list = userDauStatisticeService.queryUserStatistics(map);
        resault.put("lists", list);
        //默认查询7日新增
        UserDauStatisticsVo vo = userDauStatisticeService.queryUserStatisticsGather(map);
        //默认查询7日活跃数
        Double brisk = userService.queryUserBriskNumber(map);
        vo.setUserGather(brisk);//活跃数
        Double validDrisk = 0.0;
        //按时间查询活跃用户
        List<User> activeUserList = userService.dauStatistic(map);
        for (int i = 0; i < activeUserList.size(); i++) {
            int id = activeUserList.get(i).getId();//用户id
            map.put("id", id);
            //根据userid查询用户是否进行过上述行为
            int followsum = userService.queryFollow(map);//是否关注过圈子、标签或作者
            int postsum = userService.queryPost(map);//是否发过贴
            int zansum = userService.queryZan(map);//是否点赞过
            int collectsum = userService.queryCollect(map);//是否收藏过
            int commentsum = userService.queryComment(map);//是否评论过
            int forwardsum = userService.queryForward(map);//是否转发过
            if (followsum > 0 || postsum > 0 || zansum > 0 || collectsum > 0 || commentsum > 0 || forwardsum > 0) {
                validDrisk++;
            }
        }
        vo.setValGather(Double.parseDouble(String.valueOf(validDrisk + "")));//有效活跃数
        vo.setActivityRate((Double.parseDouble(String.valueOf(vo.getValGather()))) / (Double.parseDouble(String.valueOf(vo.getUserGather()))));//活跃率
        resault.put("gather", vo);
        return resault;
    }

    /**
     * 统计用户渠道数量
     *
     * @return
     */
    public List<UserChannelStatistics> queryUserChannelStatistics() {
        return userService.queryUserChannelStatistics();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException calendar 对日期进行时间操作
     *                        getTimeInMillis() 获取日期的毫秒显示形式
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    public Map queryPostStatistics(String time) {
        Map resault = new HashMap();
        Date begin = null;
        Date end = null;
        Map map = new HashMap();
        if (StringUtil.isNotEmpty(time)) {
            String[] dates = time.split(",");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                begin = format.parse(dates[0]);
                end = format.parse(dates[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
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


    /**
     * 调价查询平台访问日志
     *
     * @param memberId
     * @param pager
     * @return
     */
    public List<AccessLog> findAllPlatformAccess(String memberId, Paging<AccessLog> pager) {
        AccessLog accessLog = new AccessLog();
        if (StringUtil.isNotEmpty(memberId)) {
            accessLog.setMemberid(Integer.parseInt(memberId));
        }
        return accessLogService.findAllqueryPlatformAccess(accessLog, pager);
    }

    /**
     * 查询平台访问用户列表
     *
     * @return
     */
    public List<User> queryPlatformAccessByUserList() {
        return userService.queryPlatformAccessByUserList();
    }
}
