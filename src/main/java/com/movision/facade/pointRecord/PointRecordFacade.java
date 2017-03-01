package com.movision.facade.pointRecord;

import com.movision.common.constant.PointConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.mybatis.pointRecord.entity.DailyTask;
import com.movision.mybatis.pointRecord.entity.NewTask;
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
     * 增加积分流水
     *
     * @param point   分数
     * @param isadd   加减（0增加 1减少）
     * @param type    积分类型
     * @param orderid 订单id--对应下单赚积分
     * @return
     */
    public int addPointRecord(int point, int isadd, int type, String orderid) {
        PointRecord pointRecord = new PointRecord();
        pointRecord.setUserid(ShiroUtil.getAppUserID());
        pointRecord.setIsadd(isadd);
        pointRecord.setPoint(point);
        pointRecord.setType(type);
        pointRecord.setOrderid(StringUtils.isEmpty(orderid) ? 0 : Integer.valueOf(orderid));

        return pointRecordService.addPointRecord(pointRecord);
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
                } else if (type == PointConstant.POINT_TYPE.first_share.getCode()) {
                    newTask.setFirstShare(true);
                } else if (type == PointConstant.POINT_TYPE.first_comment.getCode()) {
                    newTask.setFirstComment(true);
                } else if (type == PointConstant.POINT_TYPE.first_support.getCode()) {
                    newTask.setFirstSupport(true);
                } else if (type == PointConstant.POINT_TYPE.first_post.getCode()) {
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
