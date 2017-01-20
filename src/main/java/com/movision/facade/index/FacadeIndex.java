package com.movision.facade.index;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.service.HomepageManageService;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/17 16:05
 */
@Service
public class FacadeIndex {

    @Autowired
    private PostService postService;

    @Autowired
    private HomepageManageService homepageManageService;

    public Map<String, Object> queryIndexData(String userid) {

        HashMap<String, Object> map = new HashMap();

        HomepageManage homepageMainBanner = homepageManageService.queryBanner(2);//查询首页最上方的主banner type=2
        List<PostVo> todayEssenceList = postService.queryTodayEssence();//查询今日精选
        List<PostVo> yesterdayEssenceList = postService.queryDayAgoEssence(1);//查询昨日精选
        List<PostVo> beforeYesterdayEssenceList = postService.queryDayAgoEssence(2);//查询前日精选
        List<PostVo> threeDayEssenceList = postService.queryDayAgoEssence(3);//查询大前日精选
        HomepageManage homepageManage = homepageManageService.queryBanner(0);//查询你可能喜欢的banner图 type=0
        List<Circle> mayLikeCircleList;
        if (!StringUtils.isEmpty(userid)) {
            mayLikeCircleList = postService.queryMayLikeCircle(Integer.parseInt(userid));//登录后查询可能喜欢的圈子
        } else {
            mayLikeCircleList = postService.queryRecommendCircle();//未登录查询我们推荐的圈子
        }

        map.put("homepageMainBanner", homepageMainBanner);
        map.put("todayEssenceList", todayEssenceList);
        map.put("yesterdayEssenceList", yesterdayEssenceList);
        map.put("beforeYesterdayEssenceList", beforeYesterdayEssenceList);
        map.put("threeDayEssenceList", threeDayEssenceList);
        map.put("homepageManage", homepageManage);
        map.put("mayLikeCircleList", mayLikeCircleList);

        return map;
    }

}
