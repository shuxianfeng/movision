package com.movision.facade.rewarded;


import com.movision.facade.pointRecord.PointRecordFacade;
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
    PointRecordFacade pointRecordFacade;
    @Autowired
    PostService postService;
    @Autowired
    CircleService circleService;
    @Autowired
    UserService userService;
    @Autowired
    RewardedService rewardedService;

    public int updateRewarded(String postid, String integral, String userid) {
        int i = pointRecordFacade.addPointRecord(Integer.parseInt(integral), null, userid);//当前登录用户
        int uid = postService.queryUserByPostid(postid);
        pointRecordFacade.addPointRecord(Integer.parseInt(integral), null, userid);//被打赏用户
        return i;
    }
}
