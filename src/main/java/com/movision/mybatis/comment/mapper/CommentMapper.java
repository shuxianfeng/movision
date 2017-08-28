package com.movision.mybatis.comment.mapper;

import com.movision.mybatis.comment.entity.Comment;
import com.movision.mybatis.comment.entity.CommentCount;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.entity.ReplyComment;
import com.movision.mybatis.user.entity.User;
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

    int updateByPrimaryKeySelective(CommentVo record);

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

    List<CommentVo> queryCommentByUserid(Integer userid);//根据userid查询评论

    List<CommentVo> findAllQueryComment(Integer userid, RowBounds rowBounds);

    List<CommentVo> findQueryComment(Integer userid);
    List<CommentVo> queryPidComment(Integer pid);

    Integer updateCommentRead(Integer userid);

    Integer queryUseridByComment(Integer commentid);

    Integer updateCommentHeatValue(Map map);

    int queryCommentLevel(int commentid);


    List<CommentVo> queryCommentByPost(int postid);

    List<CommentVo> findAllCommentByPostId(int postid, RowBounds rowBounds);

    List<CommentVo> findAllCommentByPost(int postid);
    Integer queryCommentIsRead(int userid);//查询评论未读

    CommentCount queryCommentZan(int id);

    CommentVo queryCom(int commentid);

    int commentCount(Map map);

    int deleteComment(int id);

    int lessPostComment(int id);

    List<CommentVo> findAllOneComment(int postid, RowBounds rowBounds);

    List<CommentVo> queryTwoComment(int pid);

    List<CommentVo> queryThreeComment(Map<String, Object> parammap);

    User queryUserInfor(int pid);

    int repliesNumber(int id);

    List<ReplyComment> selectReplyCommentList(Integer userid);

    void updateCommentIsRead(Integer id);


}