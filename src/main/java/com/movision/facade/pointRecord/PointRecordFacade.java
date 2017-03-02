package com.movision.facade.pointRecord;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.constant.PointConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.exception.BusinessException;
import com.movision.mybatis.pointRecord.entity.DailyTask;
import com.movision.mybatis.pointRecord.entity.NewTask;
import com.movision.mybatis.pointRecord.entity.PersonPointStatistics;
import com.movision.mybatis.pointRecord.entity.PointRecord;
import com.movision.mybatis.pointRecord.service.PointRecordService;
import com.movision.utils.ListUtil;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private PointRecordService pointRecordService;


    /**
     * 获取个人积分统计
     *
     * @param pointRecordList 个人积分数据列表：个人历史积分列表 or 个人今日积分列表
     * @return
     */
    private PersonPointStatistics getMyTotalPointStatics(List<PointRecord> pointRecordList) {

        int rewardCount = 0, postCount = 0, commentCount = 0, shareCount = 0;
        if (ListUtil.isNotEmpty(pointRecordList)) {
            for (PointRecord pointRecord : pointRecordList) {
                int oldtype = null == pointRecord.getType() ? 0 : pointRecord.getType();
                if (oldtype == PointConstant.POINT_TYPE.reward.getCode()) {
                    rewardCount++;
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
     * 增加积分流水
     * <p>
     * 下面几种特殊情况，积分会不一样
     * 1 打赏 每日前5次，每次加5分
     * 2 发帖 每日前5次，每次加5分
     * 3 评论 每日前10次，每次加2分
     * 4 分享，每日前10次，每次加5分
     * <p>
     * 其他情况正常加分
     *
     * @param type    积分类型
     * @param orderid 订单id--对应下单赚积分
     * @return
     */
    public int addPointRecord(int type, String orderid) {

        //获取每日的积分数据
        List<PointRecord> todayList = pointRecordService.queryMyTodayPoint(ShiroUtil.getAppUserID());
        PersonPointStatistics todayStatistics = this.getMyTotalPointStatics(todayList);
        //个人历史积分数据
        List<PointRecord> historyList = pointRecordService.queryAllMyPointRecord(ShiroUtil.getAppUserID());
        PersonPointStatistics historyStatistics = this.getMyTotalPointStatics(historyList);

        int new_point = getPointByPointType(type, todayStatistics, historyStatistics);

        PointRecord pointRecord = new PointRecord();
        pointRecord.setUserid(ShiroUtil.getAppUserID());
        pointRecord.setIsadd(PointConstant.POINT_ADD);
        pointRecord.setPoint(new_point);
        pointRecord.setType(type);
        pointRecord.setOrderid(StringUtils.isEmpty(orderid) ? 0 : Integer.valueOf(orderid));

        return pointRecordService.addPointRecord(pointRecord);
    }

    /**
     * 根据积分类型，分配对应的加分数值
     *
     * @param type

     * @return
     */
    private int getPointByPointType(int type, PersonPointStatistics todayStatistics, PersonPointStatistics historyStatistics) {
        int new_point = 0;
        int rewardCount = todayStatistics.getRewardCount(),
                postCount = todayStatistics.getPostCount(),
                commentCount = todayStatistics.getCommentCount(),
                shareCount = todayStatistics.getShareCount();

//        int rewardCount = todayStatistics.getRewardCount(),
//                postCount = todayStatistics.getPostCount(),
//                commentCount = todayStatistics.getCommentCount(),
//                shareCount = todayStatistics.getShareCount();

        /**
         * 根据积分类型，分配对应的加分数值
         */
        if (type == PointConstant.POINT_TYPE.reward.getCode()) {
            if (rewardCount >= 0 && rewardCount <= 4) {
                new_point = PointConstant.POINT.reward.getCode();
            } else {
                new_point = 0;
            }

        } else if (type == PointConstant.POINT_TYPE.post.getCode()) {

            if (postCount == 0) {
                new_point = PointConstant.POINT.first_post.getCode();
            } else if (postCount >= 1 && postCount <= 4) {
                new_point = PointConstant.POINT.post.getCode();
            } else {
                new_point = 0;
            }

        } else if (type == PointConstant.POINT_TYPE.comment.getCode()) {
            if (commentCount == 0) {
                new_point = PointConstant.POINT.first_comment.getCode();
            } else if (commentCount >= 1 && commentCount <= 4) {
                new_point = PointConstant.POINT.comment.getCode();
            } else {
                new_point = 0;
            }

        } else if (type == PointConstant.POINT_TYPE.share.getCode()) {
            if (shareCount == 0) {
                new_point = PointConstant.POINT.first_share.getCode();
            } else if (shareCount >= 1 && shareCount <= 4) {
                new_point = PointConstant.POINT.share.getCode();
            } else {
                new_point = 0;
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

        } else if (type == PointConstant.POINT_TYPE.index_selected.getCode()) {
            new_point = PointConstant.POINT.index_selected.getCode();

        } else if (type == PointConstant.POINT_TYPE.circle_selected.getCode()) {
            new_point = PointConstant.POINT.circle_selected.getCode();

        } else {
            throw new BusinessException(MsgCodeConstant.app_point_type_not_exist, "积分类型不存在");
        }
        return new_point;
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
            // TODO: 2017/3/2
//            for (PointRecord pointRecord : pointRecordList) {
//                //获取积分的类型
//                int type = null == pointRecord.getType() ? 0 : pointRecord.getType();
//
//                if (type == PointConstant.POINT_TYPE.new_user_register.getCode()) {
//                    newTask.setRegister(true);
//                } else if (type == PointConstant.POINT_TYPE.finish_personal_data.getCode()) {
//                    newTask.setFinishPersonalData(true);
//                } else if (type == PointConstant.POINT_TYPE.binding_phone.getCode()) {
//                    newTask.setBindPhone(true);
//                } else if (type == PointConstant.POINT_TYPE.first_focus.getCode()) {
//                    newTask.setFirstFocus(true);
//                } else if (type == PointConstant.POINT_TYPE.first_collect.getCode()) {
//                    newTask.setFirstCollect(true);
//                } else if (type == PointConstant.POINT_TYPE.first_share.getCode()) {
//                    newTask.setFirstShare(true);
//                } else if (type == PointConstant.POINT_TYPE.first_comment.getCode()) {
//                    newTask.setFirstComment(true);
//                } else if (type == PointConstant.POINT_TYPE.first_support.getCode()) {
//                    newTask.setFirstSupport(true);
//                } else if (type == PointConstant.POINT_TYPE.first_post.getCode()) {
//                    newTask.setFirstPost(true);
//                } else if (type == PointConstant.POINT_TYPE.comment_app.getCode()) {
//                    newTask.setCommentApp(true);
//                } else {
//                    continue;
//                }
//            }
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
                int type = pointRecord.getType();
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
                    rewardCount++;

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

    public Boolean signToday() {
        int count = pointRecordService.queryIsSignToday(ShiroUtil.getAppUserID());
        return count == 1;
    }

    public List<Map> findAllMyPointList(Paging<Map> paging) {
        Map map = new HashedMap();
        map.put("userid", ShiroUtil.getAppUserID());
        return pointRecordService.findAllMyPointList(paging, map);
    }


}
