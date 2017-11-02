package com.movision.facade.index;

import com.movision.common.constant.HeatValueConstant;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.followLabel.service.FollowLabelService;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postHeatvalueRecord.entity.PostHeatvalueRecord;
import com.movision.mybatis.postHeatvalueRecord.service.PostHeatvalueRecordService;
import com.movision.mybatis.systemLayout.service.SystemLayoutService;
import com.movision.mybatis.user.entity.TalentUserVo;
import com.movision.mybatis.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    private FollowLabelService followLabelService;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private PostHeatvalueRecordService postHeatvalueRecordService;
    @Autowired
    private SystemLayoutService systemLayoutService;

    /**
     * 增加帖子热度值 + 记录帖子热度变化流水（公共方法）
     */
    public void addHeatValue(int postid, int type, int userid) {
        int points = 0;
        Map map = new HashMap();
        map.put("postid", postid);
        if (type == 1) {//首页精选
            int isessence = postService.queryPostIsessenceHeat(postid);
            if (isessence == 1) {//首页精选(实时)
                //首页精选的热度值
                //points = HeatValueConstant.POINT.home_page_selection.getCode();
                points = systemLayoutService.queryHaetValue("heat_home_page_selection");
                map.put("points", points);
                postService.updatePostHeatValue(map);

                addPostHeatvalueRecord(userid, points, HeatValueConstant.HEATVALUE_TYPE.home_page_selection.getCode(), postid);
            }
        } else if (type == 2) {//帖子精选(实时ok)
            int hot = postService.queryPostHotHeat(postid);
            if (hot == 1) {
                //points = HeatValueConstant.POINT.circle_selection.getCode();
                points = systemLayoutService.queryHaetValue("heat_circle_selection");
                map.put("points", points);
                postService.updatePostHeatValue(map);

                addPostHeatvalueRecord(userid, points, HeatValueConstant.HEATVALUE_TYPE.circle_selection.getCode(), postid);
            }
        } else if (type == 3) {//点赞(实时ok)
            int level = userLevels(userid);
            //points = level * HeatValueConstant.POINT.zan_number.getCode();
            points = systemLayoutService.queryHaetValue("heat_zan_number");
            map.put("points", points);
            postService.updatePostHeatValue(map);

            addPostHeatvalueRecord(userid, points, HeatValueConstant.HEATVALUE_TYPE.zan_number.getCode(), postid);

        } else if (type == 4) {//评论ok
            //查询这个帖子的所有评论的热度值
            int level = userLevels(userid);
            //points = level * HeatValueConstant.POINT.comments_number.getCode();
            points = systemLayoutService.queryHaetValue("heat_comments_number");
            map.put("points", points);
            postService.updatePostHeatValue(map);

            addPostHeatvalueRecord(userid, points, HeatValueConstant.HEATVALUE_TYPE.comments_number.getCode(), postid);

        } else if (type == 5) {//转发(实时ok)
            int level = userLevels(userid);
            //points = level * HeatValueConstant.POINT.forwarding_number.getCode();
            points = systemLayoutService.queryHaetValue("heat_forwarding_number");
            map.put("points", points);
            postService.updatePostHeatValue(map);

            addPostHeatvalueRecord(userid, points, HeatValueConstant.HEATVALUE_TYPE.forwarding_number.getCode(), postid);

        } else if (type == 6) {//收藏(实时ok)
            int level = userLevels(userid);
            //points = level * HeatValueConstant.POINT.collection_number.getCode();
            points = systemLayoutService.queryHaetValue("heat_collection_number");
            map.put("points", points);
            postService.updatePostHeatValue(map);

            addPostHeatvalueRecord(userid, points, HeatValueConstant.HEATVALUE_TYPE.collection_number.getCode(), postid);

        } else if (type == 7) {//打赏(实时ok)
            int level = userLevels(userid);
            //points = level * HeatValueConstant.POINT.reward_post.getCode();
            points = systemLayoutService.queryHaetValue("heat_reward_post");
            map.put("points", points);
            postService.updatePostHeatValue(map);

            addPostHeatvalueRecord(userid, points, HeatValueConstant.HEATVALUE_TYPE.reward_post.getCode(), postid);

        } else if (type == 8) {//帖子浏览数
            //points = HeatValueConstant.POINT.view_post.getCode();
            points = systemLayoutService.queryHaetValue("heat_view_post");
            map.put("points", points);
            postService.updatePostHeatValue(map);

            addPostHeatvalueRecord(userid, points, HeatValueConstant.HEATVALUE_TYPE.view_post.getCode(), postid);
        }

    }

    private void addPostHeatvalueRecord(int userid, int points, int type, int postid) {
        PostHeatvalueRecord postHeatvalueRecord = new PostHeatvalueRecord();
        postHeatvalueRecord.setIntime(new Date());  //时间戳
        postHeatvalueRecord.setHeatValue(points);   //变动的热度
        postHeatvalueRecord.setIsadd(0);    //0表示增加
        postHeatvalueRecord.setIsdel(0);    //0表示未删除
        postHeatvalueRecord.setUserid(userid);    //操作的用户id
        postHeatvalueRecord.setPostid(postid);
        postHeatvalueRecord.setType(type);    //操作的类型

        postHeatvalueRecordService.add(postHeatvalueRecord);
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
        if (heatvalue >= 50) {
            int level = userLevels(userid);
            points = level * HeatValueConstant.POINT.collection_number.getCode();
            map.put("points", points);
            postService.updateZanPostHeatValue(map);
        } else {
            postService.updateZeroHeatValue(postid);
        }

    }

    /**
     * 取消用户热度
     */
    public void lessUserHeatValue(int type, int userid) {
        int points = 0;
        Map map = new HashMap();
        map.put("userid", userid);
        int heatvalue = postService.selectUserHeatValue(userid);
        if (type == 1) {//用户粉丝
            if (heatvalue >= 20) {
                int level = userLevels(userid);
                //points = level * HeatValueConstant.POINT.fan_count.getCode();
                points = level * systemLayoutService.queryHaetValue("heat_fan_count");
                map.put("points", points);
                postService.lessUserHeatValue(map);
            } else {
                postService.updateZeroUserHeatValue(userid);
            }
        } else if (type == 2) {//发帖数
            if (heatvalue >= 50) {
                int level = userLevels(userid);
                //points = level * HeatValueConstant.POINT.posts_count.getCode();
                points = level * systemLayoutService.queryHaetValue("heat_posts_count");
                map.put("points", points);
                postService.lessUserHeatValue(map);
            } else {
                postService.updateZeroUserHeatValue(userid);
            }
        }
    }

    /**
     * 取消标签热度
     */
    public void lessLabelHeatValue(int labelid, String userid) {
        int points = 0;
        Map map = new HashMap();
        map.put("labelid", labelid);
        int heatvalue = followLabelService.queryLabelLabel(labelid);
        if (heatvalue >= 50) {
            int level = userLevels(Integer.parseInt(userid));
            //points = level * HeatValueConstant.POINT.attention_label.getCode();
            points = level * systemLayoutService.queryHaetValue("heat_attention_label");
            map.put("points", points);
            followLabelService.lessLabelHeatValue(map);
        } else {
            followLabelService.lessZeroLabel(labelid);
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
            //points = level * HeatValueConstant.POINT.fan_count.getCode();
            points = level * systemLayoutService.queryHaetValue("heat_fan_count");
            map.put("points", points);
            userService.updateUserHeatValue(map);
        } else if (type == 2) {//发帖数
            int level = userLevels(userid);
            //points = level * HeatValueConstant.POINT.posts_count.getCode();
            points = level * systemLayoutService.queryHaetValue("heat_posts_count");
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
            //points = level * HeatValueConstant.POINT.attention_label.getCode();
            points = level * systemLayoutService.queryHaetValue("heat_attention_label");
            map.put("points", points);
            followLabelService.updateLabelHeatValue(map);
        } else if (type == 2) {
            //发帖用到标签ok
            //points = HeatValueConstant.POINT.using_label.getCode();
            points = systemLayoutService.queryHaetValue("heat_using_label");
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
            //points = level * HeatValueConstant.POINT.reply_comment_number.getCode();
            points = level * systemLayoutService.queryHaetValue("heat_reply_comment_number");
            map.put("points", points);
            commentService.updateCommentHeatValue(map);
        } else if (type == 2) {//評論的點贊數
            int level = commentUserLevels(commentid);
            //points = level * HeatValueConstant.POINT.comment_zan_count.getCode();
            points = level * systemLayoutService.queryHaetValue("heat_comment_zan_count");
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
        TalentUserVo talentUserVo = new TalentUserVo();
        int point = commentService.queryCommentLevel(commentid);
        //判断用户等级
        Map<String, Integer> resmap = userFacade.getLev(point);
        talentUserVo.setLevel(resmap.get("lev"));
        //查询level
        //int lev=commentService.queryCommentPoints(commentid);
        int le = resmap.get("lev");
        int level = level(le);
        return level;
    }


    /**
     * 查询用户等级
     *
     * @param userid
     * @return
     */
    public int userLevels(int userid) {
        TalentUserVo talentUserVo = new TalentUserVo();
        int point = userService.queryUserLevel(userid);
        //判断用户等级
        Map<String, Integer> resmap = userFacade.getLev(point);
        talentUserVo.setLevel(resmap.get("lev"));
        int le = resmap.get("lev");
        //查询level
        //  int lev=userService.queryUserPoints(userid);
        int level = level(le);
        return level;
    }


    /**
     * 等级
     *
     * @param level
     * @return
     */
    public int level(int level) {
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
     }else if (level == 10) {
     level = 11;
     }else if (level == 11) {
     level = 12;
     }else if (level == 12) {
     level = 13;
     }else if (level == 13) {
     level = 14;
     }else if (level == 14) {
     level = 15;
     }else if (level == 15) {
     level = 16;
     }else if (level == 16) {
     level = 17;
     }else if (level == 17) {
     level = 18;
     }else if (level == 18) {
     level = 19;
     }else if (level == 19) {
     level = 20;
     }else if (level == 20) {
     level = 21;
     }else if (level == 21) {
     level = 22;
     }else if (level == 22) {
     level = 23;
     }else if (level == 23) {
     level = 24;
     }else if (level == 24) {
     level = 25;
     }else if (level == 25) {
     level = 26;
     }else if (level == 26) {
     level = 27;
     }else if (level == 27) {
     level = 28;
     }else if (level == 28) {
     level = 29;
     }else if (level == 29) {
     level = 30;
     }else if (level == 30) {
     level = 31;
     }else if (level == 31) {
     level = 32;
     }else if (level == 32) {
     level = 33;
     }else if (level == 33) {
     level = 34;
     }else if (level == 34) {
     level = 35;
     }else if (level == 35) {
     level = 36;
     }else if (level == 36) {
     level = 37;
     }else if (level == 37) {
     level = 38;
     }else if (level == 38) {
     level = 39;
     }else if (level == 39) {
     level = 40;
     }else if (level == 40) {
     level = 41;
     }else if (level == 41) {
     level = 42;
     }else if (level == 42) {
     level = 43;
     }else if (level == 43) {
     level = 44;
     }else if (level == 44) {
     level = 45;
     }else if (level == 45) {
     level = 46;
     }else if (level == 46) {
     level = 47;
     }else if (level == 47) {
     level = 48;
     }else if (level == 48) {
     level = 49;
     }else if (level == 49) {
     level = 50;
     }else if (level == 50) {
     level = 51;
     }else if (level == 51) {
     level = 52;
     }else if (level == 52) {
     level = 53;
     }else if (level == 53) {
     level = 54;
     }else if (level == 54) {
     level = 55;
     }else if (level == 55) {
     level = 56;
     }else if (level == 56) {
     level = 57;
     }else if (level == 57) {
     level = 58;
     }else if (level == 58) {
     level = 59;
     }else if (level == 59) {
     level = 60;
     }else if (level == 60) {
     level = 61;
     }else if (level == 61) {
     level = 62;
     }else if (level == 62) {
     level = 63;
     }else if (level == 63) {
     level = 64;
     }else if (level == 64) {
     level = 65;
     }else if (level == 65) {
     level = 66;
     }else if (level == 66) {
     level = 67;
     }else if (level == 67) {
     level = 68;
     }else if (level == 68) {
     level = 69;
     }else if (level == 69) {
     level = 70;
     }else if (level == 70) {
     level = 71;
     }else if (level == 71) {
     level = 72;
     }else if (level == 72) {
     level = 73;
     }else if (level == 73) {
     level = 74;
     }else if (level == 74) {
     level = 75;
     }else if (level == 75) {
     level = 76;
     }else if (level == 76) {
     level = 77;
     }else if (level == 77) {
     level = 78;
     }else if (level == 78) {
     level = 79;
     }else if (level == 79) {
     level = 80;
     }else if (level == 80) {
     level = 81;
     }else if (level == 81) {
     level = 82;
     }else if (level == 82) {
     level = 83;
     }else if (level == 83) {
     level = 84;
     }else if (level == 84) {
     level = 85;
     }else if (level == 85) {
     level = 86;
     }else if (level == 86) {
     level = 87;
     }else if (level == 87) {
     level = 88;
     }else if (level == 88) {
     level = 89;
     }else if (level == 89) {
     level = 90;
     }else if (level == 90) {
     level = 91;
     }else if (level == 91) {
     level = 92;
     }else if (level == 92) {
     level = 93;
     }else if (level == 93) {
     level = 94;
     }else if (level == 94) {
     level = 95;
     }else if (level == 95) {
     level = 96;
     }else if (level == 96) {
     level = 97;
     }else if (level == 97) {
     level = 98;
     }else if (level == 98) {
     level = 99;
     }else if (level == 99) {
     level = 100;
     }else if (level == 100) {
     level = 101;
        }
        return level;
    }

}
