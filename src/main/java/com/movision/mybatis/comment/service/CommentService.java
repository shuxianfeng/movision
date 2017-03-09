package com.movision.mybatis.comment.service;

import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.mapper.CommentMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/1/22 15:35
 */
@Service
@Transactional
public class CommentService {

    private static Logger log = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    public CommentMapper commentMapper;

    public List<CommentVo> queryCommentsByLsit(Paging<CommentVo> pager, Map map) {

        try {
            log.info("查询某个帖子的评论列表");
            return commentMapper.findAllqueryCommentsByLsit(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("查询帖子评论列表失败");
            throw e;
        }
    }

    public int queryIsZan(Map<String, Object> parammap) {
        try {
            log.info("查询该用户是否点赞过该评论");
            return commentMapper.queryIsZan(parammap);
        } catch (Exception e) {
            log.error("查询该用户是否点赞过该评论失败");
            throw e;
        }
    }

    /**
     * 查询评论详情
     *
     * @param pager
     * @return
     */
    public List<CommentVo> queryPostByCommentParticulars(Map<String, Integer> map, Paging<CommentVo> pager) {
        try {
            log.info("查询评论详情");
            return commentMapper.findAllQueryPostByCommentParticulars(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询评论详情异常");
            throw e;
        }
    }

    /**
     * 查询评论
     *
     * @param map
     * @return
     */
    public CommentVo queryCommentById(Map map) {
        try {
            log.info("查询评论");
            return commentMapper.queryCommentById(map);
        } catch (Exception e) {
            log.error("查询评论异常");
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

    public void insertCommentZanRecord(Map<String, Object> parammap) {
        try {
            log.info("插入一条评论点赞记录");
            commentMapper.insertCommentZanRecord(parammap);
        } catch (Exception e) {
            log.error("插入一条评论点赞记录失败");
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
/*
    public List<CommentVo> queryComments(String postid, Paging<CommentVo> pager) {
        try {
            log.info("查询评论");
            return commentMapper.findAllqueryCommentsByLsit(Integer.parseInt(postid), pager.getRowBounds());
        } catch (NumberFormatException e) {
            log.error("查询评论异常");
            throw e;
        }
    }*/

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

    /**
     * 编辑帖子评论
     *
     * @param map
     * @return
     */
    public int updatePostComment(Map map) {
        try {
            log.info("编辑帖子评论");
            return commentMapper.updatePostComment(map);
        } catch (Exception e) {
            log.error("编辑帖子评论异常");
            throw e;
        }
    }

    /**
     * 回复帖子评论
     *
     * @return
     */
    public int replyPostComment(Map map) {
        try {
            log.info("回复帖子评论");
            return commentMapper.replyPostComment(map);
        } catch (Exception e) {
            log.error("回复帖子评论异常");
            throw e;
        }
    }

    /**
     * 根据点赞排序评论列表
     *
     * @param postid
     * @return
     */
    public List<CommentVo> commentZanSork(Integer postid, Paging<CommentVo> pager) {
        try {
            log.info("根据点赞排序评论列表");
            return commentMapper.commentZanSork(postid, pager.getRowBounds());
        } catch (Exception e) {
            log.error("根据点赞排序评论列表异常");
            throw e;
        }
    }

    /**
     * 查询含有敏感字的帖子
     *
     * @param pager
     * @return
     */
    public List<CommentVo> queryCommentSensitiveWords(Map map, Paging<CommentVo> pager) {
        try {
            log.info("查询含有敏感字的帖子");
            return commentMapper.findAllQueryCommentSensitiveWords(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询含有敏感字的帖子异常");
            throw e;
        }
    }

    /**
     * 根据用户id查询用户帖子被评论的评论列表
     *
     * @param userid
     * @param pager
     * @return
     */
    public List<CommentVo> queryCommentListByUserid(String userid, Paging<CommentVo> pager) {
        try {
            log.info("根据用户id查询用户帖子被评论的评论列表");
            return commentMapper.findAllQueryCommentListByUserid(userid, pager.getRowBounds());
        } catch (Exception e) {
            log.error("根据用户id查询用户帖子被评论的评论列表异常");
            throw e;
        }
    }
}
