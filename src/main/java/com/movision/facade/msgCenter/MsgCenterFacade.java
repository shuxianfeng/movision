package com.movision.facade.msgCenter;

import com.movision.mybatis.PostZanRecord.entity.PostZanRecord;
import com.movision.mybatis.PostZanRecord.entity.PostZanRecordVo;
import com.movision.mybatis.PostZanRecord.service.PostZanRecordService;
import com.movision.mybatis.comment.entity.Comment;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.imSystemInform.entity.ImSystemInform;
import com.movision.mybatis.imSystemInform.service.ImSystemInformService;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.postCommentZanRecord.entity.PostCommentZanRecord;
import com.movision.mybatis.postCommentZanRecord.entity.PostCommentZanRecordVo;
import com.movision.mybatis.postCommentZanRecord.service.PostCommentZanRecordService;
import com.movision.mybatis.rewarded.entity.Rewarded;
import com.movision.mybatis.rewarded.entity.RewardedVo;
import com.movision.mybatis.rewarded.service.RewardedService;
import com.movision.utils.L;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 消息中心
 *
 * @Author zhuangyuhao
 * @Date 2017/4/5 13:48
 */
@Service
public class MsgCenterFacade {

    private static Logger log = LoggerFactory.getLogger(MsgCenterFacade.class);

    @Autowired
    private ImSystemInformService imSystemInformService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RewardedService rewardedService;
    @Autowired
    private PostCommentZanRecordService postCommentZanRecordService;
    @Autowired
    private PostZanRecordService postZanRecordService;
    /**
     * 获取消息中心的列表
     *
     * @return
     */
    public Map getMsgCenterList(Integer userid) {
        //都是查找最新的消息
        Map reMap = new HashedMap();
        //1 赞消息 。包含：帖子，活动，评论，快问（后期）
        PostCommentZanRecordVo postCommentZanRecord = postCommentZanRecordService.queryByUserid(userid);
        PostZanRecordVo postZanRecord = postZanRecordService.queryByUserid(userid);
        if (postCommentZanRecord != null && postZanRecord == null) {
            reMap.put("postCommentZanRecord", postCommentZanRecord);
        } else if (postCommentZanRecord == null && postZanRecord != null) {
            reMap.put("postZanRecord", postZanRecord);
        } else if (postCommentZanRecord != null && postZanRecord != null) {
            Date time = postCommentZanRecord.getIntime();
            Date date = postZanRecord.getIntime();
            if (time != null && date != null) {
                long begin = time.getTime();
                long end = date.getTime();
                if (begin > end) {
                    reMap.put("postCommentZanRecord", postCommentZanRecord);
                } else if (end > begin) {
                    reMap.put("postZanRecord", postZanRecord);
                }
            }
        }
        //2 打赏消息
        RewardedVo rewarded = rewardedService.queryRewardByUserid(userid);
        //3 评论消息
        CommentVo comment = commentService.queryCommentByUserid(userid);
        //4 系统通知
        ImSystemInform imSystemInform = imSystemInformService.queryByUserid();
        //5 打招呼消息

        //6 客服消息
        reMap.put("imSystemInform", imSystemInform);
        reMap.put("comment", comment);
        reMap.put("rewarded", rewarded);
        return reMap;
    }

    /**
     * 获取系统通知列表
     *
     * @return
     */
    public List<ImSystemInform> getMsgInformationList(Paging<ImSystemInform> paging) {

        List<ImSystemInform> list = imSystemInformService.queryAll(paging);
        return list;
    }

    /**
     * 获取评论列表
     *
     * @param userid
     * @param pager
     * @return
     */
    public List<CommentVo> getMsgCommentList(Integer userid, Paging<CommentVo> pager) {
        List<CommentVo> comments = commentService.findAllQueryComment(userid, pager);
        if (comments != null) {
            for (int i = 0; i < comments.size(); i++) {
                Integer pid = comments.get(i).getPid();
                if (pid == null) {
                    Integer postid = comments.get(i).getPostid();
                    List<Post> post = postZanRecordService.queryPost(postid);
                    comments.get(i).setPost(post);
                } else if (pid != null) {
                    List<CommentVo> commentVos = commentService.queryPidComment(pid);
                    comments.get(i).setCommentVos(commentVos);
                }
            }
        }
        return comments;
    }

    /**
     * 获取打赏列表
     *
     * @param userid
     * @param pager
     * @return
     */
    public List<RewardedVo> getMsgRewardedList(Integer userid, Paging<RewardedVo> pager) {
        List<RewardedVo> rewardedVos = rewardedService.findAllRewarded(userid, pager);
        if (rewardedVos != null) {
            for (int i = 0; i < rewardedVos.size(); i++) {
                Integer postid = rewardedVos.get(i).getPostid();
                List<Post> post = postZanRecordService.queryPost(postid);
                rewardedVos.get(i).setPosts(post);
            }
        }

        return rewardedVos;
    }

    /**
     * 点赞列表
     *
     * @param userid
     * @return
     */
    public Map getMsgZanList(Integer userid) {
        List<PostCommentZanRecordVo> postCommentZanRecords = postCommentZanRecordService.findAllCommentZanList(userid);
        int type = 0;
        Map map = new HashedMap();
        if (postCommentZanRecords != null) {
            for (int i = 0; i < postCommentZanRecords.size(); i++) {
                Integer commentid = postCommentZanRecords.get(i).getCommentid();
                List<CommentVo> commentVo = postCommentZanRecordService.queryComment(commentid);
                postCommentZanRecords.get(i).setComment(commentVo);
                postCommentZanRecords.get(i).setCtype(1);
            }
        }
        List<PostZanRecordVo> postZanRecordVos = postZanRecordService.findAllZanList(userid);
        if (postZanRecordVos != null) {
            for (int i = 0; i < postZanRecordVos.size(); i++) {
                Integer postid = postZanRecordVos.get(i).getPostid();
                List<Post> post = postZanRecordService.queryPost(postid);
                postZanRecordVos.get(i).setPosts(post);
                postZanRecordVos.get(i).setCtype(2);
            }
        }
        map.put("postCommentZanRecords", postCommentZanRecords);
        map.put("postZanRecordVos", postZanRecordVos);
        return map;
    }




}
