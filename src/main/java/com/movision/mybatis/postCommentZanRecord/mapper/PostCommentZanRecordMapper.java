package com.movision.mybatis.postCommentZanRecord.mapper;

import com.movision.mybatis.PostZanRecord.entity.ZanRecordVo;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.postCommentZanRecord.entity.PostCommentZanRecord;
import com.movision.mybatis.postCommentZanRecord.entity.PostCommentZanRecordVo;
import com.movision.mybatis.user.entity.User;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface PostCommentZanRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostCommentZanRecord record);

    int insertSelective(PostCommentZanRecord record);

    PostCommentZanRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostCommentZanRecord record);

    int updateByPrimaryKey(PostCommentZanRecord record);

    PostCommentZanRecordVo queryByUserid(Integer userid);


    List<CommentVo> queryComment(Integer commentid);

    List<ZanRecordVo> findAllZan(Integer userid, RowBounds rowBounds);

    User queryusers(Integer userid);
}