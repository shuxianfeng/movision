package com.movision.facade.rewarded;


import com.movision.common.constant.PointConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.constant.entity.Constant;
import com.movision.mybatis.constant.service.ConstantService;
import com.movision.mybatis.pointRecord.service.PointRecordService;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.record.service.RecordService;
import com.movision.mybatis.rewarded.entity.Rewarded;
import com.movision.mybatis.rewarded.service.RewardedService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/1/23 15:52
 */
@Service
public class FacadeRewarded {
    @Autowired
    PointRecordFacade pointRecordFacade;

    @Autowired
    PostService postService;

    @Autowired
    ConstantService constantService;

    @Autowired
    PointRecordService pointRecordService;

    @Autowired
    UserFacade userFacade;

    @Autowired
    RecordService recordService;

    @Autowired
    RewardedService rewardedService;


    /**
     * 公共方法 保存积分流水记录
     *
     * @param userid
     * @param point
     * @param isadd
     * @param
     * @return
     */
    private int rewardRecord(int userid, int point, int isadd) {
        Map map = new HashedMap();

        map.put("userid", userid);//用户id
        map.put("point", point);//积分
        map.put("isadd", isadd);//增加或减少0加1减
        map.put("type", PointConstant.POINT_TYPE.reward.getCode());//积分变动类型（打赏:12）
        map.put("orderid", 0);//订单相关id
        map.put("intime", new Date());//插入时间
        map.put("isdel", 0);//是否删除
        return recordService.insertRewardRecord(map);//增加积分流水记录
    }

    /**
     * 帖子打赏
     *
     * @param postid
     * @param type
     * @param userid
     * @return
     */
    @Transactional
    public Map updateRewarded(String postid, String type, String userid) {
        Integer u = Integer.parseInt(userid);
        Map map = new HashedMap();
        Rewarded rewarded = new Rewarded();
        //查询出贴主id
        int uid = postService.queryUserByPostid(postid);
        if (uid != u) {//如果当前登录用户id和打赏帖子的贴主相同，则不可以打赏
            int integral = pointRecordFacade.getIntegral(Integer.parseInt(type));//获取类型对应的积分
            int in = userFacade.queryUserByRewarde(u);//查询出当前用户积分剩余
            if (in >= integral) {
                int playtour = pointRecordService.queryMyTodayPointSum(u);//查询当天打赏了多少次
                if (playtour < 5) {
                    //每天打赏次数在五次以内，参加每日活动，给赠送积分者增加5积分,并且增加积分流水记录
                    userFacade.updateUserPoint(u, 5, PointConstant.POINT_ADD);//给用户增加每日活动积分
                    rewardRecord(u, 5, PointConstant.POINT_ADD);//保存积分流水记录
                    in += 5;//为当前登录用户增加活动积分
                }
                //为打赏者减去积分
                int i = userFacade.updateUserPoint(u, integral, PointConstant.POINT_DECREASE);
                rewardRecord(u, integral, PointConstant.POINT_DECREASE);//保存积分流水记录
                if (i == 1) {
                    in -= integral;
                }
                //int uid = postService.queryUserByPostid(postid);//查询出被打赏用户
                //为帖主增加积分
                userFacade.updateUserPoint(uid, integral, PointConstant.POINT_ADD);
                rewardRecord(uid, integral, PointConstant.POINT_ADD);//保存积分流水记录

                rewarded.setPostid(Integer.parseInt(postid));//帖子id
                rewarded.setUserid(u);//打赏 人
                rewarded.setIntime(new Date());//打赏时间
                rewarded.setPoints(integral);//打赏分数
                //帖子打赏记录流水
                rewardedService.insertRewarded(rewarded);

                //重复了，不需要
//                pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.reward.getCode());//完成积分任务根据不同积分类型赠送积分的公共方法（包括总分和流水）

                //2 更新session中的缓存
                ShiroUtil.updateAppuserPoint(in);
                map.put("code", 200);
                map.put("resault", in);
                return map;
            } else {
                map.put("code", 300);
                map.put("resault", in);
                return map;
            }
        } else {
            map.put("code", 400);
            return map;
        }
    }

    /**
     * 查询打赏积分数值列表
     *
     * @return
     */
    public Map queryRewordList(String userid) {
        Map map = new HashMap();
        List<Constant> constants = constantService.queryRewordList();
        //获取当前用户积分
        Integer reworded = userFacade.queryUserByRewarde(Integer.parseInt(userid));
        map.put("constants", constants);
        map.put("reworded", reworded);
        return map;
    }


}
