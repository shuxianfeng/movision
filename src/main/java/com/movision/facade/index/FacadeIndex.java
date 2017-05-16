package com.movision.facade.index;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.service.HomepageManageService;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author shuxf
 * @Date 2017/1/17 16:05
 */
@Service
public class FacadeIndex {

    private static Logger log = LoggerFactory.getLogger(FacadeIndex.class);

    @Autowired
    private PostService postService;

    @Autowired
    private HomepageManageService homepageManageService;

    @Cacheable(value = "indexData", key = "'index_data'")
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
        map.put("todayEssenceList", processEnddaysPartsum(todayEssenceList));//经过processEnddaysPartsum方法处理
        map.put("yesterdayEssenceList", processEnddaysPartsum(yesterdayEssenceList));//经过processEnddaysPartsum方法处理
        map.put("beforeYesterdayEssenceList", processEnddaysPartsum(beforeYesterdayEssenceList));//经过processEnddaysPartsum方法处理
        map.put("threeDayEssenceList", processEnddaysPartsum(threeDayEssenceList));//经过processEnddaysPartsum方法处理
        map.put("homepageManage", homepageManage);
        map.put("mayLikeCircleList", mayLikeCircleList);

        return map;
    }

    /**
     * 添加活动剩余天数和投稿总数
     *
     * @param postVoList
     * @return
     */
    public List<PostVo> processEnddaysPartsum(List<PostVo> postVoList) {
        if (postVoList.size() > 0) {
            for (int i = 0; i < postVoList.size(); i++) {
                PostVo vo = postVoList.get(i);
                //如果是活动的话，计算距结束天数和已参与人数
                if (vo.getIsactive() == 1) {
                    //遍历所有的活动开始时间和结束时间，计算活动距离结束的剩余天数
                    calculateEnddays(postVoList, i, vo);
                    //计算已投稿总数
                    int postid = vo.getId();//获取活动id
                    int partsum = postService.queryActivePartSum(postid);
                    postVoList.get(i).setPartsum(partsum);
                }
            }
            return postVoList;
        } else {
            return postVoList;
        }
    }

    /**
     * 计算活动距离结束的剩余天数
     *
     * @param postVoList
     * @param i
     * @param vo
     */
    private void calculateEnddays(List<PostVo> postVoList, int i, PostVo vo) {
        Date begin = vo.getBegintime();//活动开始时间
        Date end = vo.getEndtime();//活动结束时间
        Date now = new Date();//活动当前时间
        if (now.before(begin)) {
            vo.setEnddays(-1);//活动还未开始
        } else if (end.before(now)) {
            vo.setEnddays(0);//活动已结束
        } else if (begin.before(now) && now.before(end)) {
            try {
                log.error("计算活动剩余结束天数");
                Long between_days = DateUtils.getBetweenDays(now, end);
                postVoList.get(i).setEnddays(Integer.parseInt(String.valueOf(between_days)) - 1);
            } catch (Exception e) {
                log.error("计算活动剩余结束天数失败");
                e.printStackTrace();
            }
        }
    }

}
