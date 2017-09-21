package com.movision.mybatis.postCommentZanRecord.mapper;

import com.movision.mybatis.PostZanRecord.entity.ZanRecordVo;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.postCommentZanRecord.entity.PostCommentZanRecord;
import com.movision.mybatis.postCommentZanRecord.entity.PostCommentZanRecordVo;
import com.movision.mybatis.user.entity.User;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface PostCommentZanRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostCommentZanRecord record);

    int insertSelective(PostCommentZanRecord record);

    PostCommentZanRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ZanRecordVo record);

    int updateByPrimaryKey(PostCommentZanRecord record);

    List<CommentVo> queryComment(Integer commentid);

    List<ZanRecordVo> findZan(Integer userid);

    User queryusers(Integer userid);

    String queryPostNickname(Integer postid);
}