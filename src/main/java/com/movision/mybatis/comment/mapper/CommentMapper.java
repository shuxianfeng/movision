package com.movision.mybatis.comment.mapper;

import com.movision.mybatis.comment.entity.Comment;
import com.movision.mybatis.comment.entity.CommentVo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(CommentVo record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<CommentVo> findAllqueryCommentsByLsit(RowBounds rowBounds, Integer postid);

    int queryIsZan(Map<String, Object> parammap);

    List<CommentVo> findAllQueryPostByCommentParticulars(Map<String, Integer> map, RowBounds rowBounds);

    CommentVo queryCommentById(Map map);

    CommentVo queryChildrenComment(int id);

    void insertCommentZanRecord(Map<String, Object> parammap);

    int updateCommentZanSum(int id);

    int queryCommentZanSum(int id);

    int deletePostAppraise(int id);

    int updatePostComment(Map map);

    int replyPostComment(Map map);

    List<CommentVo> commentZanSork(Integer postid, RowBounds rowBounds);

}