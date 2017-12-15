package com.movision.mybatis.robotComment.mapper;

import com.movision.mybatis.robotComment.entity.RobotComment;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface RobotCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RobotComment record);

    int insertSelective(RobotComment record);

    RobotComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RobotComment record);

    int updateByPrimaryKey(RobotComment record);

    List<RobotComment> findAllQueryRoboltComment(RobotComment robotComment, RowBounds rowBounds);

    List<RobotComment> queryRoboltComment(Map map);

    RobotComment queryCommentById(Integer id);

    void updateByCommentById(RobotComment robotComment);

    Integer queryComentMessage(RobotComment robotComment);

    void updateRoboltComentByTypeIsNull(RobotComment comment);
}