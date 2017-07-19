package com.movision.facade.index;

import com.movision.common.constant.HeatValueConstant;
import com.movision.mybatis.PostZanRecord.service.PostZanRecordService;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.opularSearchTerms.entity.OpularSearchTermsVo;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.user.service.UserService;
import com.movision.mybatis.userRefreshRecord.entity.UesrreflushCount;
import com.movision.mybatis.userRefreshRecord.service.UserRefreshRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/7/11 11:05
 */
@Service
public class FacadeHeatValue {
    private static Logger log = LoggerFactory.getLogger(FacadeHeatValue.class);

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserRefreshRecordService userRefreshRecordService;
    /**
     * 增加帖子热度值
     */
    public void addHeatValue(int postid, int type, int userid) {
        int points = 0;
        Map map = new HashMap();
        map.put("postid", postid);
        if (type == 1) {//首页精选
            int isessence = postService.queryPostIsessenceHeat(postid);
            if (isessence == 1) {//首页精选(实时)
                points = HeatValueConstant.POINT.home_page_selection.getCode();
                map.put("points", points);
                postService.updatePostHeatValue(map);
            }
        } else if (type == 2) {//帖子精选(实时)
            int hot = postService.queryPostHotHeat(postid);
            if (hot == 1) {
                points = HeatValueConstant.POINT.post_selection.getCode();
                map.put("points", points);
                postService.updatePostHeatValue(map);
            }
        } else if (type == 3) {//点赞(实时)
            int level = userLevels(userid);//
            points = level * HeatValueConstant.POINT.zan_number.getCode();
            map.put("points", points);
            postService.updatePostHeatValue(map);
        } else if (type == 4) {//评论
            //查询这个帖子的所有评论的热度值
            List<Integer> list = postService.queryPostComment(postid);
            for (int i = 0; i < list.size(); i++) {
                points += list.get(i);//热度值加起来
            }
            int point = (int) (points * 0.2);
            map.put("points", point);
            // postService.updatePostHeatValue(map);
        } else if (type == 5) {//转发(实时)
            int level = userLevels(userid);
            points = level * HeatValueConstant.POINT.forwarding_number.getCode();
            map.put("points", points);
            postService.updatePostHeatValue(map);
        } else if (type == 6) {//收藏(实时)
            int level = userLevels(userid);
            points = level * HeatValueConstant.POINT.collection_number.getCode();
            map.put("points", points);
            postService.updatePostHeatValue(map);
        } else if (type == 7) {//打赏(实时)
            int level = userLevels(userid);
            points = level * HeatValueConstant.POINT.reward_post.getCode();
            map.put("points", points);
            postService.updatePostHeatValue(map);
        } else if (type == 8) {//帖子浏览数
            List<UesrreflushCount> list = userRefreshRecordService.postcount(postid);
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                count = list.get(i).getCount();
            }
            points = count * HeatValueConstant.POINT.read_post.getCode();
            int point = (int) (points * 0.1);
            map.put("points", point);
            //postService.updatePostHeatValue(map);
        }

    }

    /**
     * 取消收藏时减少热度
     *
     * @param postid
     */
    public void zanLessenHeatValue(int postid, int userid) {
        int points = 0;
        Map map = new HashMap();
        int heatvalue = postService.selectPostHeatValue(postid);
        if (heatvalue != 0) {
            int level = userLevels(userid);
            points = level * HeatValueConstant.POINT.collection_number.getCode();
            map.put("points", points);
            postService.updateZanPostHeatValue(map);
        }

    }


    /**
     * 用户热度
     * @param type
     * @param userid
     */
    public void addUserHeatValue(int type, int userid) {
        int points = 0;
        Map map = new HashMap();
        map.put("userid", userid);
        if (type == 1) {//用户粉丝数
            int level = userLevels(userid);
            points = level * HeatValueConstant.POINT.fan_count.getCode();
            map.put("points", points);
            userService.updateUserHeatValue(map);
        } else if (type == 2) {//发帖数
            //查询用户发的所有帖子
            List<Integer> list = postService.queryPostUserHeatValue(userid);
            for (int i = 0; i < list.size(); i++) {
                points += list.get(i);
            }
            int point = (int) (points * 0.1);
            map.put("points", point);
        }
    }

    /**
     * 评论的热度
     *
     * @param type
     * @param commentid
     */
    public void addCommentHeatValue(int type, int commentid) {
        int points = 0;
        Map map = new HashMap();
        map.put("userid", commentid);
        if (type == 1) {//回覆的評論數
            int level = commentUserLevels(commentid);
            points = level * HeatValueConstant.POINT.reply_comment_number.getCode();
            map.put("points", points);
            commentService.updateCommentHeatValue(map);
        } else if (type == 2) {//評論的點贊數
            int level = commentUserLevels(commentid);
            points = level * HeatValueConstant.POINT.comment_zan_count.getCode();
            map.put("points", points);
            commentService.updateCommentHeatValue(map);
        }
    }


    /**
     * 查詢評論人等級
     *
     * @param commentid
     * @return
     */
    public int commentUserLevels(int commentid) {
        int level = commentService.queryCommentLevel(commentid);
        level(level);
        return level;
    }


    /**
     * 查询用户等级
     *
     * @param userid
     * @return
     */
    public int userLevels(int userid) {
        //查询发帖人级别
        int level = userService.queryUserLevel(userid);
        level(level);
        return level;
    }


    /**
     * 等级
     *
     * @param level
     * @return
     */
    public int level(int level){
        if (level == 0) {
            level = 1;
        } else if (level == 1) {
            level = 2;
        } else if (level == 2) {
            level = 3;
        } else if (level == 3) {
            level = 4;
        } else if (level == 4) {
            level = 5;
        } else if (level == 5) {
            level = 6;
        } else if (level == 6) {
            level = 7;
        } else if (level == 7) {
            level = 8;
        } else if (level == 8) {
            level = 9;
        } else if (level == 9) {
            level = 10;
        }
        return level;
    }


}
