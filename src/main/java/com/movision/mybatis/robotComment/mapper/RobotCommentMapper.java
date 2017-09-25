package com.movision.mybatis.robotComment.mapper;

import com.movision.mybatis.robotComment.entity.RobotComment;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface RobotCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RobotComment record);

    int insertSelective(RobotComment record);

    RobotComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RobotComment record);

    int updateByPrimaryKey(RobotComment record);

    List<RobotComment> findAllQueryRoboltComment(Integer type, RowBounds rowBounds);

    String queryRoboltCommentById(Integer id);

    RobotComment queryCommentById(Integer id);

    void updateByCommentById(RobotComment robotComment);

    Integer queryComentMessage(RobotComment robotComment);
}