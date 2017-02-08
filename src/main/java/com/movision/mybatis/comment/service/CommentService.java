package com.movision.mybatis.comment.service;

import com.movision.mybatis.comment.entity.Comment;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.mapper.CommentMapper;
import com.movision.mybatis.rewarded.entity.Rewarded;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/1/22 15:35
 */
@Service
public class CommentService {

    private static Logger log = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    public CommentMapper commentMapper;

    public List<CommentVo> queryCommentsByLsit(String postid, Paging<CommentVo> pager){

        try {
            log.info("查询某个帖子的评论列表");
            return commentMapper.queryCommentsByLsit(Integer.parseInt(postid),pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询帖子评论列表失败");
            throw e;
        }
    }

    public CommentVo queryChildrenComment(int id){
        try {
            log.info("查询子评论");
            return commentMapper.queryChildrenComment(id);
        } catch (Exception e) {
            log.error("查询子评论失败");
            throw e;
        }
    }

    public int updateCommentZanSum(int id) {
        try {
            log.info("更新评论点赞次数");
            return commentMapper.updateCommentZanSum(id);
        } catch (Exception e) {
            log.error("评论点赞次数更新异常");
            throw e;
        }
    }

    public int queryCommentZanSum(int id) {
        try {
            log.info("查看评论次数");
            return commentMapper.queryCommentZanSum(id);
        } catch (Exception e) {
            log.error("查询评论次数失败");
            throw e;
        }
    }

    public int insertComment(CommentVo vo) {
        try {
            log.info("插入帖子评论");
            return commentMapper.insertSelective(vo);
        } catch (Exception e) {
            log.error("帖子评论失败");
            throw e;
        }
    }

    public List<CommentVo> queryComments(String postid, Paging<CommentVo> pager) {
        try {
            log.info("查询评论");
            return commentMapper.queryCommentsByLsit(Integer.parseInt(postid), pager.getRowBounds());
        } catch (NumberFormatException e) {
            log.error("查询评论异常");
            throw e;
        }
    }

    /**
     * 删除帖子评论
     *
     * @param id
     * @return
     */
    public int deletePostAppraise(Integer id) {
        try {
            log.info("删除帖子评论");
            return commentMapper.deletePostAppraise(id);
        } catch (Exception e) {
            log.error("帖子评论删除异常");
            throw e;
        }
    }
}
