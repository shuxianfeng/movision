package com.movision.facade.label;

import com.movision.facade.index.FacadePost;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.entity.CircleCount;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.followLabel.entity.FollowLabel;
import com.movision.mybatis.followLabel.service.FollowLabelService;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.postLabel.entity.*;
import com.movision.mybatis.postLabel.service.PostLabelService;
import com.movision.mybatis.postLabelRelation.service.PostLabelRelationService;
import com.movision.mybatis.userDontLike.entity.UserDontLike;
import com.movision.mybatis.userDontLike.service.UserDontLikeService;
import com.movision.utils.DateUtils;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author zhanglei
 * @Date 2017/7/25 14:47
 */
@Service
public class LabelFacade {
    private static Logger log = LoggerFactory.getLogger(LabelFacade.class);
    @Autowired
    private PostLabelService postLabelService;
    @Autowired
    private PostLabelRelationService postLabelRelationService;
    @Autowired
    private FollowLabelService followLabelService;
    @Autowired
    private FacadePost facadePost;
    @Autowired
    private UserDontLikeService userDontLikeService;

    /**
     * 我的--关注--关注的标签，点击关注调用的关注的标签列表返回接口
     */
    public List<PostLabelVo> getMineFollowLabel(String userid, Paging<PostLabelVo> pager){
        //查询当前用户所关注的所有标签列表
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("userid", Integer.parseInt(userid));
        List<PostLabelVo> followLabelList = postLabelService.getMineFollowLabel(parammap, pager);

        //遍历查询当前用户是否关注过该标签
        for (int i=0; i<followLabelList.size(); i++){
            PostLabelVo vo = followLabelList.get(i);
            int labelid = vo.getLabelid();
            parammap.put("labelid", labelid);
            int sum = followLabelService.yesOrNo(parammap);
            vo.setIsfollow(sum);
            followLabelList.set(i, vo);
        }

        return followLabelList;
    }

    /**
     * 点击标签页上部分
     *
     * @param labelid
     * @return
     */
    public PostLabelTz labelPage(int labelid) {
        //查询头像和名称
        PostLabelTz postLabel = postLabelService.queryName(labelid);
        //根据id查询帖子数量
        int count = postLabelRelationService.labelPost(labelid);
        postLabel.setCount(count);
        //多少人关注
        int follow = postLabelRelationService.followlabel(labelid);
        postLabel.setFollow(follow);
        return postLabel;
    }


    /**
     * 点击标签页下部分
     *
     * @param type
     * @param paging
     * @param labelid
     * @return
     */
    public List postLabelList(int type, Paging<PostVo> paging, int labelid) {
        List list = null;
        //根据标签id查询帖子
        List<Integer> postid = postLabelRelationService.postList(labelid);
        if (type == 1) {//推荐
            //根据所有的id查询帖子按热度排序
            list = postLabelRelationService.postHeatValue(postid, paging);
            list = labelResult(list);
        } else if (type == 2) {//最新
            //根据所有的id查询帖子按时间排序
            list = postLabelRelationService.post(postid, paging);
            list = labelResult(list);
        } else if (type == 3) {//精华
            list = postLabelRelationService.postIseecen(postid, paging);
            list = labelResult(list);
        }
        return list;
    }


    /**
     * 关注标签
     *
     * @param userid
     * @param labelid
     * @return
     */
    public int attentionLabel(int userid, int labelid) {
        Map map = new HashMap();
        //查询当前用户有没有关注过该标签
        map.put("userid", userid);
        map.put("labelid", labelid);
        int result = followLabelService.yesOrNo(map);
        if (result == 0) {//没有关注
            FollowLabel followLabel = new FollowLabel();
            followLabel.setUserid(userid);
            followLabel.setLabelid(labelid);
            followLabel.setIntime(new Date());
            int count = followLabelService.insertSelective(followLabel);
            //增加关注标签数量
            followLabelService.updatePostLabel(labelid);
            return 0;
        } else {//关注过了
            return 1;
        }
    }

    /**
     * 返回数据
     *
     * @param
     * @param
     * @return
     */
    public List labelResult(List<PostVo> list) {
        if (list != null) {
            facadePost.findUser(list);
            facadePost.findPostLabel(list);
            facadePost.countView(list);
            facadePost.findAllCircleName(list);
        }
        return list;
    }


    /**
     * 标签达人
     *
     * @param labelid
     * @return
     */
    public List tagMan(int labelid) {
        //根据id查询用的最多的用户
        List<PostLabelCount> labelCounts = postLabelService.queryCountLabelName(labelid);
        return labelCounts;
    }


    /**
     * 圈子标签上半部分
     *
     * @param
     * @return
     */
    public CircleVo queryCircleByPostid(String circleid) {
        //根据id查询圈子所有
        CircleVo circleVo = postLabelService.queryCircleByPostid(Integer.parseInt(circleid));
        //根据圈子查询今日这个圈子的发帖数
        int todayPost = postLabelService.postInCircle(Integer.parseInt(circleid));
        circleVo.setTodayPost(todayPost);
        //查询圈子下所有帖子用的标签
        List<PostLabel> postLabels = postLabelService.queryLabelCircle(Integer.parseInt(circleid));
        circleVo.setPostLabels(postLabels);
        return circleVo;
    }


    /**
     * 圈子标签下半部分
     *
     * @param type
     * @param paging
     * @param circleid
     * @return
     */
    public List queryCircleBotton(int type, Paging<PostVo> paging, String circleid) {
        List<PostVo> list = null;
        if (type == 1) {//最热
            list = postLabelService.findAllHotPost(Integer.parseInt(circleid), paging);
            list = labelResult(list);
        } else if (type == 2) {//最新
            list = postLabelService.findAllNewPost(Integer.parseInt(circleid), paging);
            list = labelResult(list);
        } else if (type == 3) {//精华
            list = postLabelService.findAllIsencePost(Integer.parseInt(circleid), paging);
            list = labelResult(list);
        }
        return list;
    }


    /**
     * 圈子达人
     *
     * @param circleid
     * @return
     */
    public List circleOfPeople(int circleid) {
        List<CircleCount> circleCounts = postLabelService.queryCirclePeople(circleid);
        return circleCounts;
    }


    /**
     * 首页点x
     *
     * @param type
     */
    public int userDontLike(int type, int userid, int postid) {
        int count = 0;
        if (type == 1) {//内容差
            //查询该帖子的热度值
            int heat_value = postLabelService.queryPostHeatValue(postid);
            if (heat_value >= 10) {
                //根据帖子id降低热度值
                count = postLabelService.updatePostHeatValue(postid);
            } else {
                count = postLabelService.heatvale(postid);
            }
        } else if (type == 2) {//不喜欢该作者
            //根据帖子id查询作者
            int userids = postLabelService.queryUserid(postid);
            //根据id查询热度
            int heats = postLabelService.queryUserHeatValue(userids);
            if (heats >= 10) {
                //根据id降低热度值
                count = postLabelService.updateUserHeatValue(userids);
            } else {
                count = postLabelService.userHeatVale(userids);
            }
        } else if (type == 3) {//不喜欢该圈子
            //根据帖子id查询该圈子
            int circleid = postLabelService.queryCircleid(postid);
            //根据圈子id查询帖子
            List<PostVo> list = postLabelService.queryCircleByPost(circleid);
            for (int i = 0; i < list.size(); i++) {
                int post = list.get(i).getId();
                //查询该帖子的热度值
                int heat_value = postLabelService.queryPostHeatValue(post);
                if (heat_value >= 10) {
                    //根据帖子id降低热度值
                    count = postLabelService.updatePostHeatValue(post);
                } else {
                    count = postLabelService.heatvale(post);
                }
            }
        } else if (type == 4) {//就是不喜欢
            //查询该帖子的热度值
            int heat_value = postLabelService.queryPostHeatValue(postid);
            if (heat_value >= 10) {
                //根据帖子id降低热度值
                count = postLabelService.updatePostHeatValue(postid);
            } else {
                count = postLabelService.heatvale(postid);
            }
        }
        //插入mongodb
        UserDontLike userDontLike = new UserDontLike();
        userDontLike.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
        userDontLike.setIntime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        userDontLike.setPostid(postid);
        userDontLike.setUserid(userid);  //不登录的情况下，返回0
        userDontLike.setType(type);
        userDontLikeService.insert(userDontLike);
        return count;
    }


    /**
     * 取消关注标签
     *
     * @param userid
     * @param labelid
     * @return
     */
    public int cancelFollowLabel(String userid, String labelid) {
        //定义一个返回标志位
        int mark = 0;
        Map map = new HashMap();
        map.put("userid", userid);
        map.put("labelid", labelid);
        int result = followLabelService.cancleLabel(map);
        if (result == 1) {
            mark = 1;
        } else {
            mark = -1;
        }
        return mark;
    }


}
