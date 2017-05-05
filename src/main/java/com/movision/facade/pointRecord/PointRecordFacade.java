package com.movision.facade.pointRecord;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.constant.PointConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.exception.BusinessException;
import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.orders.service.OrderService;
import com.movision.mybatis.pointRecord.entity.DailyTask;
import com.movision.mybatis.pointRecord.entity.NewTask;
import com.movision.mybatis.pointRecord.entity.PersonPointStatistics;
import com.movision.mybatis.pointRecord.entity.PointRecord;
import com.movision.mybatis.pointRecord.service.PointRecordService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.shiro.realm.ShiroRealm;
import com.movision.utils.ListUtil;
import com.movision.utils.MathUtil;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/1 11:00
 */
@Service
public class PointRecordFacade {

    private static Logger log = LoggerFactory.getLogger(PointRecordFacade.class);

    @Autowired
    private PointRecordService pointRecordService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;



    /**
     * 获取个人积分统计
     * rewardCount, postCount, commentCount, shareCount
     * 打赏数量，发帖数量,评论数量，分享数量
     *
     * @param pointRecordList 个人积分数据列表：个人历史积分列表 or 个人今日积分列表
     * @return
     */
    private PersonPointStatistics getMyTotalPointStatics(List<PointRecord> pointRecordList) {

        int rewardCount = 0, postCount = 0, commentCount = 0, shareCount = 0;
        if (ListUtil.isNotEmpty(pointRecordList)) {
            for (PointRecord pointRecord : pointRecordList) {
                int oldtype = null == pointRecord.getType() ? 0 : pointRecord.getType();
                int isadd = pointRecord.getIsadd();
                if (oldtype == PointConstant.POINT_TYPE.reward.getCode()) {
                    if (isadd == 1) {
                        rewardCount++;
                    }
                } else if (oldtype == PointConstant.POINT_TYPE.post.getCode()) {
                    postCount++;
                } else if (oldtype == PointConstant.POINT_TYPE.comment.getCode()) {
                    commentCount++;
                } else if (oldtype == PointConstant.POINT_TYPE.share.getCode()) {
                    shareCount++;
                } else {
                    //不考虑
                    continue;
                }
            }
        }
        PersonPointStatistics personPointStatistics = new PersonPointStatistics(rewardCount, postCount, commentCount, shareCount);
        return personPointStatistics;
    }


    /**
     * 增加积分流水+增加用户积分+改变session用户积分
     * <p>
     * 下面几种特殊情况，积分会不一样
     * 1 打赏 每日前5次，每次加5分
     * 2 发帖 每日前5次，每次加5分
     * 3 评论 每日前10次，每次加2分;
     *          PS：若今日的第一次评论是首次评论，则积分=首次评论的积分+每日评论的积分（分享和发帖类似）
     * 4 分享，每日前10次，每次加5分
     * <p>
     * 其他情况正常加分
     *
     * @param type    积分类型
     *                （无下单送积分，该积分流水已经在下单接口中处理了）
     * @param
     * @return
     */
    public void addPointRecord(int type, int userid) {

        log.info("调用【增加积分流水】接口，本次操作存在积分变动");
        //获取每日的积分数据
        List<PointRecord> todayList = pointRecordService.queryMyTodayPoint(userid);
        PersonPointStatistics todayStatistics = this.getMyTotalPointStatics(todayList);
        //个人历史积分数据
        List<PointRecord> historyList = pointRecordService.queryAllMyPointRecord(userid);
        PersonPointStatistics historyStatistics = this.getMyTotalPointStatics(historyList);

        //获取需要新增的积分
        int new_point = getPointByPointType(type, todayStatistics, historyStatistics);
        log.info("【增加积分流水】该积分类型type=" + type + ", 该类型对应的积分是：" + new_point);
        if (new_point != 0) {
            //增加积分流水
            log.debug("【addPointRecord】session中的userid:" + ShiroUtil.getAppUserID());
            log.debug("【addPointRecord】session中的用户信息：" + ShiroUtil.getAppUser());

            addPointRecord(type, new_point, userid);
            //新增个人积分
            addPersonPointInDbAndSession(new_point, userid);
        }
    }

    /**
     * 帖子精选，首页精选时的积分操作
     *
     * @param type
     * @param userid 发帖人id
     */
    public void addPointForCircleAndIndexSelected(int type, int userid) {

        if (userid != -1) {
            //走正常积分操作
            if (PointConstant.POINT_TYPE.circle_selected.getCode() == type) {
                //圈子精选
                doSelectedPointProcess(type, userid, PointConstant.POINT.circle_selected.getCode());

            } else {
                //首页精选
                doSelectedPointProcess(type, userid, PointConstant.POINT.index_selected.getCode());
            }
        }
    }

    /**
     * 处理帖子精选，首页精选的积分变动
     *
     * @param type
     * @param userid
     * @param point
     */
    private void doSelectedPointProcess(int type, int userid, int point) {
        //添加积分流水
        addPointRecord(type, point, userid);
        //增加用户积分
        User user = userService.selectByPrimaryKey(userid);
        if (null != user) {
            int originPoint = user.getPoints();
            user.setPoints(originPoint + point);
            userService.updateByPrimaryKeySelective(user);
        }
    }


    /**
     * 增加用户积分+改变session用户积分 (数据库+session)
     *
     * @param point 需要加的积分
     */
    public void addPersonPointInDbAndSession(int point, int userid) {

        User user = userService.selectByPrimaryKey(userid);

        int personPoint = user.getPoints();
        int newPoint = personPoint + point;

        //1 变更用户表积分信息
        user.setPoints(newPoint);
        userService.updateByPrimaryKeySelective(user);
        //2 更新session中的缓存
        ShiroUtil.updateAppuserPoint(newPoint);

    }

    /**
     * 仅仅增加积分流水
     *
     * @param type
     */
    public void addPointRecordOnly(int type, int userid) {

        log.info("调用【增加积分流水】接口，本次操作存在积分变动");
        //获取每日的积分数据
        List<PointRecord> todayList = pointRecordService.queryMyTodayPoint(userid);
        PersonPointStatistics todayStatistics = this.getMyTotalPointStatics(todayList);
        //获取历史的积分数据
        List<PointRecord> historyList = pointRecordService.queryAllMyPointRecord(userid);
        PersonPointStatistics historyStatistics = this.getMyTotalPointStatics(historyList);

        //获取需要新增的积分
        int new_point = getPointByPointType(type, todayStatistics, historyStatistics);
        log.info("【增加积分流水】该积分类型type=" + type + ", 该类型对应的积分是：" + new_point);
        //增加积分流水
        if (new_point != 0) {
            addPointRecord(type, new_point, userid);
        }

    }

    /**
     * 增加积分流水
     *
     * @param type
     * @param new_point
     */
    private void addPointRecord(int type, int new_point, int userid) {
        PointRecord pointRecord = new PointRecord();
        pointRecord.setUserid(userid);    //当前操作积分的APP用户
        pointRecord.setIsadd(PointConstant.POINT_ADD);
        pointRecord.setPoint(new_point);
        pointRecord.setType(type);
        pointRecord.setOrderid(0);

        pointRecordService.addPointRecord(pointRecord);
    }


    /**
     * 根据积分类型，计算出对应的加分数值
     *
     * （无下单送积分，该积分流水已经在下单接口中处理了）
     *
     * @param type
     * @return
     */
    private int getPointByPointType(int type, PersonPointStatistics todayStatistics, PersonPointStatistics historyStatistics) {
        int new_point = 0;
        int rewardCount = todayStatistics.getRewardCount(),
                postCount = todayStatistics.getPostCount(), //今日发帖数
                commentCount = todayStatistics.getCommentCount(),   //今日评论数
                shareCount = todayStatistics.getShareCount();   //今日分享数

        int historyPostCount = historyStatistics.getPostCount(),  //历史发帖数
                historyCommentCount = historyStatistics.getCommentCount(),  //历史评论数
                historyShareCount = historyStatistics.getShareCount();  //历史分享数

        /**
         * 打赏的积分
         */
        if (type == PointConstant.POINT_TYPE.reward.getCode()) {
            if (rewardCount >= 0 && rewardCount <= 4) {
                new_point = PointConstant.POINT.reward.getCode();
            } else {
                new_point = 0;
            }

            /**
             * 每日发帖的得分算法
             */
        } else if (type == PointConstant.POINT_TYPE.post.getCode()) {
            if (historyPostCount == 0) {
                //历史上无发帖纪录
                if (postCount == 0) {   //今日无发帖数
                    //首次发帖=首次发帖积分+发帖积分
                    new_point = PointConstant.POINT.first_post.getCode() + PointConstant.POINT.post.getCode();
                } else if (postCount >= 1 && postCount <= 4) {
                    //已经存在首次发帖，并且发帖数不超过4个，每日发帖拿积分的上限是5个，所以新的发帖可以拿到积分
                    new_point = PointConstant.POINT.post.getCode();
                } else {
                    //每日发帖数已经超过了每日发帖拿积分的上限，即5个，所以新的发帖拿不到积分
                    new_point = 0;
                }
            } else {
                //历史上存在发帖纪录
                if (postCount >= 0 && postCount <= 4) {
                    //每日发帖数不超过4个，而每日发帖拿积分的上限数是5个，所以新的发帖可以拿到积分
                    new_point = PointConstant.POINT.post.getCode();
                } else {
                    //每日发帖数已经超过了每日发帖拿积分的上限，即5个，所以新的发帖拿不到积分
                    new_point = 0;
                }
            }

            /**
             * 每日评论和每日发帖同理
             */
        } else if (type == PointConstant.POINT_TYPE.comment.getCode()) {
            if (historyCommentCount == 0) {
                if (commentCount == 0) {
                    //首次评论
                    new_point = PointConstant.POINT.first_comment.getCode() + PointConstant.POINT.comment.getCode();
                } else if (commentCount >= 1 && commentCount <= 4) {
                    new_point = PointConstant.POINT.comment.getCode();
                } else {
                    new_point = 0;
                }
            } else {
                if (commentCount >= 0 && commentCount <= 4) {
                    new_point = PointConstant.POINT.comment.getCode();
                } else {
                    new_point = 0;
                }
            }

            /**
             *  每日分享和每日发帖同理
             */
        } else if (type == PointConstant.POINT_TYPE.share.getCode()) {
            if (historyShareCount == 0) {
                if (shareCount == 0) {
                    //首次分享
                    new_point = PointConstant.POINT.first_share.getCode() + PointConstant.POINT.share.getCode();
                } else if (shareCount >= 1 && shareCount <= 4) {
                    new_point = PointConstant.POINT.share.getCode();
                } else {
                    new_point = 0;
                }
            } else {
                if (shareCount >= 0 && shareCount <= 4) {
                    new_point = PointConstant.POINT.share.getCode();
                } else {
                    new_point = 0;
                }
            }


        } else if (type == PointConstant.POINT_TYPE.new_user_register.getCode()) {
            new_point = PointConstant.POINT.new_user_register.getCode();

        } else if (type == PointConstant.POINT_TYPE.finish_personal_data.getCode()) {
            new_point = PointConstant.POINT.finish_personal_data.getCode();

        } else if (type == PointConstant.POINT_TYPE.binding_phone.getCode()) {
            new_point = PointConstant.POINT.binding_phone.getCode();

        } else if (type == PointConstant.POINT_TYPE.first_focus.getCode()) {
            new_point = PointConstant.POINT.first_focus.getCode();

        } else if (type == PointConstant.POINT_TYPE.first_collect.getCode()) {
            new_point = PointConstant.POINT.first_collect.getCode();

        } else if (type == PointConstant.POINT_TYPE.first_support.getCode()) {
            new_point = PointConstant.POINT.first_support.getCode();

        } else if (type == PointConstant.POINT_TYPE.comment_app.getCode()) {
            new_point = PointConstant.POINT.comment_app.getCode();

        } else if (type == PointConstant.POINT_TYPE.sign.getCode()) {
            new_point = PointConstant.POINT.sign.getCode();

            //这两个类型是在后台操作
        /*} else if (type == PointConstant.POINT_TYPE.index_selected.getCode()) {
            new_point = PointConstant.POINT.index_selected.getCode();

        } else if (type == PointConstant.POINT_TYPE.circle_selected.getCode()) {
            new_point = PointConstant.POINT.circle_selected.getCode();*/

            //这个类型是在下订单的时候操作
        /*} else if (type == PointConstant.POINT_TYPE.place_order.getCode()) {
            //订单赚积分，根据订单的消费总金额折算成积分，100元=1积分
            Orders orders = orderService.getOrderById(orderid);
            if (null == orders) {
                throw new BusinessException(MsgCodeConstant.NOT_EXIST_ORDER, "不存在该订单");
            }
            double realMoney = null == orders.getRealmoney() ? 0.0 : orders.getRealmoney();
            new_point = MathUtil.division100ToInteger(realMoney);*/

        } else {
            throw new BusinessException(MsgCodeConstant.app_point_type_not_exist, "积分类型不存在");
        }
        return new_point;
    }

    public int getIntegral(int type) {
        int integral = 0;
        if (type == PointConstant.REWARD_TYPE.reward_10.getCode()) {
            integral = PointConstant.REWARD_POINT.point_10.getCode();

        } else if (type == PointConstant.REWARD_TYPE.reward_20.getCode()) {
            integral = PointConstant.REWARD_POINT.point_20.getCode();

        } else if (type == PointConstant.REWARD_TYPE.reward_50.getCode()) {
            integral = PointConstant.REWARD_POINT.point_50.getCode();

        } else if (type == PointConstant.REWARD_TYPE.reward_100.getCode()) {
            integral = PointConstant.REWARD_POINT.point_100.getCode();

        } else if (type == PointConstant.REWARD_TYPE.reward_233.getCode()) {
            integral = PointConstant.REWARD_POINT.point_233.getCode();

        } else if (type == PointConstant.REWARD_TYPE.reward_666.getCode()) {
            integral = PointConstant.REWARD_POINT.point_666.getCode();
        }
        return integral;
    }

    /**
     * 获取新手任务、每日任务的完成情况
     *
     * @return
     */
    public Map<String, Object> getMyAllTypePoint() {
        Map map = new HashedMap();

        NewTask newTask = getNewTask();
        map.put("newTask", newTask);

        DailyTask dailyTask = getDailyTask();
        map.put("dailyTask", dailyTask);
        return map;
    }

    /**
     * 获取新手任务的完成情况
     *
     * @return
     */
    private NewTask getNewTask() {
        List<PointRecord> pointRecordList = pointRecordService.queryAllMyPointRecord(ShiroUtil.getAppUserID());

        NewTask newTask = new NewTask(false, false, false, false, false, false, false, false, false, false);
        if (ListUtil.isNotEmpty(pointRecordList)) {
            //有积分流水 
            for (PointRecord pointRecord : pointRecordList) {
                //获取积分的类型
                int type = null == pointRecord.getType() ? 0 : pointRecord.getType();

                if (type == PointConstant.POINT_TYPE.new_user_register.getCode()) {
                    newTask.setRegister(true);
                } else if (type == PointConstant.POINT_TYPE.finish_personal_data.getCode()) {
                    newTask.setFinishPersonalData(true);
                } else if (type == PointConstant.POINT_TYPE.binding_phone.getCode()) {
                    newTask.setBindPhone(true);
                } else if (type == PointConstant.POINT_TYPE.first_focus.getCode()) {
                    newTask.setFirstFocus(true);
                } else if (type == PointConstant.POINT_TYPE.first_collect.getCode()) {
                    newTask.setFirstCollect(true);
                } else if (type == PointConstant.POINT_TYPE.share.getCode()) {
                    //只要是分享过，肯定存在首次分享
                    newTask.setFirstShare(true);

                } else if (type == PointConstant.POINT_TYPE.comment.getCode()) {
                    //只要是评论过，肯定存在首次评论
                    newTask.setFirstComment(true);

                } else if (type == PointConstant.POINT_TYPE.first_support.getCode()) {
                    newTask.setFirstSupport(true);
                } else if (type == PointConstant.POINT_TYPE.post.getCode()) {
                    //只要发帖，肯定存在首次发帖
                    newTask.setFirstPost(true);

                } else if (type == PointConstant.POINT_TYPE.comment_app.getCode()) {
                    newTask.setCommentApp(true);
                } else {
                    continue;
                }
            }
        }
        return newTask;
    }

    /**
     * 获取每日任务的完成情况
     *
     * @return
     */
    private DailyTask getDailyTask() {
        DailyTask dailyTask = new DailyTask(0, false, 0, false, 0, false, 0, false, 0, false, 0, false, 0, false);
        List<PointRecord> todayPointList = pointRecordService.queryMyTodayPoint(ShiroUtil.getAppUserID());
        if (ListUtil.isNotEmpty(todayPointList)) {
            //初始化计算值
            int rewardCount = 0, postCount = 0, commentCount = 0, shareCount = 0;
            for (PointRecord pointRecord : todayPointList) {
                int type = pointRecord.getType();   //积分类型
                int isadd = pointRecord.getIsadd(); //加减（0增加 1减少）
                if (type == PointConstant.POINT_TYPE.sign.getCode()) {
                    dailyTask.setSign(true);
                    dailyTask.setSignCount(1);
                } else if (type == PointConstant.POINT_TYPE.index_selected.getCode()) {
                    dailyTask.setIndexSelectedCount(1);
                    dailyTask.setIndexSelected(true);
                } else if (type == PointConstant.POINT_TYPE.circle_selected.getCode()) {
                    dailyTask.setCircleSelected(true);
                    dailyTask.setCircleSelectedCount(1);
                } else if (type == PointConstant.POINT_TYPE.reward.getCode()) {
                    if (isadd == 1) {
                        //只统计打赏别人的次数
                        rewardCount++;
                    }

                } else if (type == PointConstant.POINT_TYPE.post.getCode()) {
                    postCount++;

                } else if (type == PointConstant.POINT_TYPE.comment.getCode()) {
                    commentCount++;

                } else if (type == PointConstant.POINT_TYPE.share.getCode()) {
                    shareCount++;
                }
            }
            //整理统计值
            dailyTask.setRewardCount(rewardCount);
            dailyTask.setReward(rewardCount >= 5);

            dailyTask.setPostCount(postCount);
            dailyTask.setPost(postCount >= 5);

            dailyTask.setCommentCount(commentCount);
            dailyTask.setComment(commentCount >= 10);

            dailyTask.setShareCount(shareCount);
            dailyTask.setShare(shareCount >= 10);

        }
        return dailyTask;
    }

    /**
     * 判断今天是否签到
     *
     * @return 签了：true
     */
    public Boolean signToday() {
        int count = pointRecordService.queryIsSignToday(ShiroUtil.getAppUserID());
        return count == 1;
    }

    /**
     * 查找我的积分明细
     * @param paging
     * @return
     */
    public List<Map> findAllMyPointList(Paging<Map> paging) {
        Map map = new HashedMap();
        map.put("userid", ShiroUtil.getAppUserID());
        return pointRecordService.findAllMyPointList(paging, map);
    }


}
