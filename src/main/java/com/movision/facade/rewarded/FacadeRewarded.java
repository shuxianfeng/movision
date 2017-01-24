package com.movision.facade.rewarded;


import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.rewarded.entity.Rewarded;
import com.movision.mybatis.rewarded.service.RewardedService;
import com.movision.mybatis.user.service.UserService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/1/23 15:52
 */
@Service
public class FacadeRewarded {
    @Autowired
    PostService postService;
    @Autowired
    CircleService circleService;
    @Autowired
    UserService userService;
    @Autowired
    RewardedService rewardedService;

    public boolean updateRewarded(String postid, String integral, String userid) {
        int circleid = postService.queryPostByCircleid(postid);//查询帖子所属圈子
        String phone = circleService.queryCircleByPhone(circleid);//查询圈子的登录手机号
        Map<String, Object> mapadd = new HashedMap();
        mapadd.put("phone", phone);
        mapadd.put("integral", integral);
        //查询赠送者的积分是否足够
        int sum = userService.queryUserByPoints(userid);
        if (sum >= Integer.parseInt(integral)) {//积分充足
            boolean bool = userService.updateUserPointsAdd(mapadd);//给贴主增加积分
            if (bool) {
                Map<String, Object> map = new HashedMap();
                map.put("userid", userid);
                map.put("integral", integral);
                bool = userService.updateUserPointsMinus(map);//给赠送者减积分
                if (bool) {
                    Rewarded rewarded = new Rewarded();
                    rewarded.setIntime(new Date());
                    rewarded.setPoints(Integer.parseInt(integral));
                    rewarded.setUserid(Integer.parseInt(userid));
                    rewarded.setPostid(Integer.parseInt(postid));
                    rewardedService.insertRewarded(rewarded);//添加帖子打赏记录
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
