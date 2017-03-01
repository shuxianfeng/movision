package com.movision.facade.pointRecord;

import com.movision.common.constant.PointConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.mybatis.pointRecord.entity.DailyTask;
import com.movision.mybatis.pointRecord.entity.NewTask;
import com.movision.mybatis.pointRecord.entity.PointRecord;
import com.movision.mybatis.pointRecord.service.PointRecordService;
import com.movision.utils.ListUtil;
import org.apache.commons.collections.map.HashedMap;
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
    public int addPointRecord(int point, int isadd, int type, int orderid) {
        PointRecord pointRecord = new PointRecord();
        pointRecord.setUserid(ShiroUtil.getAppUserID());
        pointRecord.setIsadd(isadd);
        pointRecord.setPoint(point);
        pointRecord.setType(type);
        pointRecord.setOrderid(orderid);

        return pointRecordService.addPointRecord(pointRecord);
    }

    /**
     * 获取新手任务和每日任务的完成情况
     *
     * @return
     */
    public Map<String, Object> getMyAllTypePoint() {
        List<PointRecord> pointRecordList = pointRecordService.queryAllMyPointRecord(ShiroUtil.getAppUserID());
        Map map = new HashedMap();
        int[] pointArray = new int[18]; //用来记录各种类型的积分分数
        NewTask newTask = new NewTask(false, false, false, false, false, false, false, false, false, false);

        if (ListUtil.isNotEmpty(pointRecordList)) {
            //有积分流水
            this.getNewTaskPoint(pointRecordList, pointArray, newTask);
        }
        map.put("newTask", newTask);

        DailyTask dailyTask = new DailyTask(0, false, 0, false, 0, false, 0, false, 0, false, 0, false, 0, false);
        List<PointRecord> todayPointList = pointRecordService.queryMyTodayPoint(ShiroUtil.getAppUserID());
        if (ListUtil.isNotEmpty(todayPointList)) {
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

                }
            }
        }
        map.put("dailyTask", dailyTask);
        return map;
    }

    /**
     * 获取新手任务的完成情况
     *
     * @param pointRecordList
     * @param pointArray
     * @param newTask
     */
    private void getNewTaskPoint(List<PointRecord> pointRecordList, int[] pointArray, NewTask newTask) {
        for (PointRecord pointRecord : pointRecordList) {
            //获取积分的类型
            int type = null == pointRecord.getType() ? 0 : pointRecord.getType();

            if (type == PointConstant.POINT_TYPE.new_user_register.getCode()) {
                pointArray[0] = 25;
                newTask.setRegister(true);
            } else if (type == PointConstant.POINT_TYPE.finish_personal_data.getCode()) {
                pointArray[1] = 15;
                newTask.setFinishPersonalData(true);
            } else if (type == PointConstant.POINT_TYPE.binding_phone.getCode()) {
                pointArray[2] = 10;
                newTask.setBindPhone(true);
            } else if (type == PointConstant.POINT_TYPE.first_focus.getCode()) {
                pointArray[3] = 10;
                newTask.setFirstFocus(true);
            } else if (type == PointConstant.POINT_TYPE.first_collect.getCode()) {
                pointArray[4] = 5;
                newTask.setFirstCollect(true);
            } else if (type == PointConstant.POINT_TYPE.first_share.getCode()) {
                pointArray[5] = 20;
                newTask.setFirstShare(true);
            } else if (type == PointConstant.POINT_TYPE.first_comment.getCode()) {
                pointArray[6] = 10;
                newTask.setFirstComment(true);
            } else if (type == PointConstant.POINT_TYPE.first_support.getCode()) {
                pointArray[7] = 5;
                newTask.setFirstSupport(true);
            } else if (type == PointConstant.POINT_TYPE.first_post.getCode()) {
                pointArray[8] = 20;
                newTask.setFirstPost(true);
            } else if (type == PointConstant.POINT_TYPE.comment_app.getCode()) {
                pointArray[9] = 100;
                newTask.setCommentApp(true);
            } else {
                continue;
            }

        }
    }
}
