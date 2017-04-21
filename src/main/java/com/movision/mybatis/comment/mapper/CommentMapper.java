package com.movision.mybatis.comment.mapper;

import com.movision.mybatis.comment.entity.Comment;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.utils.L;
import com.movision.utils.pagination.model.Paging;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(CommentVo record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<CommentVo> findAllqueryCommentsByLsit(RowBounds rowBounds, Map map);

    int queryIsZan(Map<String, Object> parammap);

    CommentVo queryCommentByPid(int pid);

    List<CommentVo> findAllQueryPostByCommentParticulars(Map<String, Integer> map, RowBounds rowBounds);

    CommentVo queryCommentById(Map map);

    CommentVo queryChildrenComment(int id);

    void insertCommentZanRecord(Map<String, Object> parammap);

    int updateCommentZanSum(int id);

    int queryCommentZanSum(int id);

    int deletePostAppraise(int id);

    int updatePostComment(Map map);

    int replyPostComment(Map map);

    Comment queryAuthenticationBypid(Integer pid);

    List<CommentVo> commentZanSork(Integer postid, RowBounds rowBounds);

    List<CommentVo> findAllQueryCommentSensitiveWords(Map map, RowBounds rowBounds);

    List<CommentVo> findAllQueryCommentListByUserid(String userid, RowBounds rowBounds);

    List<CommentVo> findAllqueryTheUserComments(String userid, RowBounds rowBounds);

    int insertComment(CommentVo commentVo);

    Integer updateCommentAudit(Map map);

    CommentVo queryCommentByUserid(Integer userid);//根据userid查询评论

    List<CommentVo> findAllQueryComment(Integer userid, RowBounds rowBounds);

    List<CommentVo> queryPidComment(Integer pid);

    Integer updateCommentRead(Integer userid);
}