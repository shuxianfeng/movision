package com.movision.facade.comment;

import com.movision.facade.index.FacadePost;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.comment.entity.Comment;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.post.service.PostService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author zhurui
 * @Date 2017/1/22 14:34
 */
@Service
public class FacadeComments {

    @Autowired
    public CommentService commentService;
    @Autowired
    public FacadePost facadePost;
    @Autowired
    public PostService postService;

    /**
     * 帖子评论列表（二级）
     *
     * @param postid
     * @return
     */
    public List<CommentVo> queryCommentsByLsit(Paging<CommentVo> pager, String postid, String type, String userid) {
        Map map = new HashedMap();
        map.put("postid", postid);
        map.put("type", type);
        List<CommentVo> volist = commentService.queryCommentsByLsit(pager, map);
        for (int i = 0; i < volist.size(); i++) {//遍历所有帖子
            //查询该用户有没有点赞该评论
            if (!StringUtil.isEmpty(userid)) {
                Map<String, Object> parammap = new HashMap<>();
                parammap.put("userid", Integer.parseInt(userid));
                parammap.put("commentid", volist.get(i).getId());
                int sum = commentService.queryIsZan(parammap);
                volist.get(i).setIsZan(sum);
            } else {
                volist.get(i).setIsZan(0);
            }

            //遍历所有评论获取父评论
            if (null != volist.get(i).getPid()) {//当评论的pid不为空的话说明是对别的评论的评论，查出父评论
                //通过pid查询评论实体
                Comment comment = commentService.queryCommentByPid(volist.get(i).getPid());
                volist.get(i).addVo(comment);
            }
        }
        return volist;
    }

    public int updateCommentZanSum(String commentid, String userid) {
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("commentid", Integer.parseInt(commentid));
        parammap.put("intime", new Date());
        commentService.insertCommentZanRecord(parammap);
        commentService.updateCommentZanSum(Integer.parseInt(commentid));//更新评论点赞次数
        int sum = commentService.queryCommentZanSum(Integer.parseInt(commentid));//查询点赞次数
        return sum;
    }

    public int insertComment(String userid, String content, String fuid, String postid) {
        if (content.length() > 500) {
            return 2;
        } else {
            int type = 0;
            if (fuid == null) {//父id为空 表示此评论是父评论
                CommentVo vo = new CommentVo();
                vo.setContent(content);
                vo.setPostid(Integer.parseInt(postid));
                vo.setUserid(Integer.parseInt(userid));
                vo.setIntime(new Date());
                vo.setZansum(0);
                vo.setIsdel("0");
                vo.setIscontribute(0);
                type = commentService.insertComment(vo);//添加评论
                //更新用户最后操作时间和帖子评论总次数
                postService.updatePostBycommentsum(Integer.parseInt(postid));//更新帖子表的评论次数字段
            } else {//表示是其他评论的子评论，不算评论次数
                CommentVo vo = new CommentVo();
                vo.setContent(content);
                vo.setPostid(Integer.parseInt(postid));
                vo.setUserid(Integer.parseInt(userid));
                vo.setIntime(new Date());
                vo.setZansum(0);
                vo.setIsdel("0");
                vo.setIscontribute(0);
                vo.setPid(Integer.parseInt(fuid));
                type = commentService.insertComment(vo);//添加评论
            }
            return type;
        }
    }
}
