package com.movision.facade.index;

import com.movision.common.constant.HeatValueConstant;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.followLabel.service.FollowLabelService;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.user.service.UserService;
import com.movision.mybatis.userRefreshRecord.service.UserRefreshRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    @Autowired
    private FollowLabelService followLabelService;
    /**
     * 增加帖子热度值
     */
    public void addHeatValue(int postid, int type, String userid) {
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
        } else if (type == 2) {//帖子精选(实时ok)
            int hot = postService.queryPostHotHeat(postid);
            if (hot == 1) {
                points = HeatValueConstant.POINT.post_selection.getCode();
                map.put("points", points);
                postService.updatePostHeatValue(map);
            }
        } else if (type == 3) {//点赞(实时ok)
            int level = userLevels(Integer.parseInt(userid));//
            points = level * HeatValueConstant.POINT.zan_number.getCode();
            map.put("points", points);
            postService.updatePostHeatValue(map);
        } else if (type == 4) {//评论ok
            //查询这个帖子的所有评论的热度值
            int level = userLevels(Integer.parseInt(userid));
            points = level * HeatValueConstant.POINT.comments_number.getCode();
            map.put("points", points);
            postService.updatePostHeatValue(map);
        } else if (type == 5) {//转发(实时ok)
            int level = userLevels(Integer.parseInt(userid));
            points = level * HeatValueConstant.POINT.forwarding_number.getCode();
            map.put("points", points);
            postService.updatePostHeatValue(map);
        } else if (type == 6) {//收藏(实时ok)
            int level = userLevels(Integer.parseInt(userid));
            points = level * HeatValueConstant.POINT.collection_number.getCode();
            map.put("points", points);
            postService.updatePostHeatValue(map);
        } else if (type == 7) {//打赏(实时ok)
            int level = userLevels(Integer.parseInt(userid));
            points = level * HeatValueConstant.POINT.reward_post.getCode();
            map.put("points", points);
            postService.updatePostHeatValue(map);
        } else if (type == 8) {//帖子浏览数
            points = HeatValueConstant.POINT.read_post.getCode();
            map.put("points", points);
            postService.updatePostHeatValue(map);
        }

    }

    /**
     * 取消收藏时减少热度ok
     *
     * @param postid
     */
    public void zanLessenHeatValue(int postid, int userid) {
        int points = 0;
        Map map = new HashMap();
        map.put("postid", postid);
        int heatvalue = postService.selectPostHeatValue(postid);
        if (heatvalue >= 15) {
            int level = userLevels(userid);
            points = level * HeatValueConstant.POINT.collection_number.getCode();
            map.put("points", points);
            postService.updateZanPostHeatValue(map);
        } else {
            postService.updateZeroHeatValue(postid);
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
        if (type == 1) {//用户粉丝数ok
            int level = userLevels(userid);
            points = level * HeatValueConstant.POINT.fan_count.getCode();
            map.put("points", points);
            userService.updateUserHeatValue(map);
        } else if (type == 2) {//发帖数
            int level = userLevels(userid);
            points = level * HeatValueConstant.POINT.posts_count.getCode();
            map.put("points", points);
            userService.updateUserHeatValue(map);
        }
    }


    /**
     * 标签热度
     * 1 关注标签时；
     * 2 发帖，使用到标签时；
     *
     * @param type
     * @param
     */
    public void addLabelHeatValue(int type, int labelid, String userid) {
        int points = 0;
        Map map = new HashMap();
        map.put("labelid", labelid);
        if (type == 1) {
            //关注标签ok
            int level = userLevels(Integer.parseInt(userid));
            points = level * HeatValueConstant.POINT.attention_label.getCode();
            map.put("points", points);
            followLabelService.updateLabelHeatValue(map);
        } else if (type == 2) {
            //发帖用到标签ok
            points = HeatValueConstant.POINT.using_label.getCode();
            map.put("points", points);
            followLabelService.updateLabelHeatValueByPost(map);
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
        map.put("commentid", commentid);
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
        int le = level(level);
        return le;
    }


    /**
     * 查询用户等级
     *
     * @param userid
     * @return
     */
    public int userLevels(int userid) {

        int level = userService.queryUserLevel(userid);
        int level1 = level(level);
        return level1;
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
