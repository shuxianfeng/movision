package com.movision.mybatis.robotComment.service;

import com.movision.mybatis.robotComment.entity.RobotComment;
import com.movision.mybatis.robotComment.mapper.RobotCommentMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/9/22 16:47
 */
@Service
public class RobotCommentService {

    static private Logger logger = LoggerFactory.getLogger(RobotCommentMapper.class);

    @Autowired
    private RobotCommentMapper robotCommentMapper;

    /**
     * 查询机器人评论列表
     *
     * @param robotComment
     * @param pag
     * @return
     */
    public List<RobotComment> findAllQueryRoboltComment(RobotComment robotComment, Paging<RobotComment> pag) {
        try {
            logger.info("查询机器人评论列表");
            return robotCommentMapper.findAllQueryRoboltComment(robotComment, pag.getRowBounds());
        } catch (Exception e) {
            logger.error("查询机器人评论列表", e);
            throw e;
        }
    }

    /**
     * 查询评论是否重复
     *
     * @param robotComment
     * @return
     */
    public Integer queryComentMessage(RobotComment robotComment) {
        try {
            logger.info("查询评论是否重复");
            return robotCommentMapper.queryComentMessage(robotComment);
        } catch (Exception e) {
            logger.error("查询评论是否重复异常", e);
            throw e;
        }
    }

    /**
     * 新增评论
     *
     * @param comment
     */
    public void insertRoboltComment(RobotComment comment) {
        try {
            logger.info("新增机器人评论");
            robotCommentMapper.insertSelective(comment);
        } catch (Exception e) {
            logger.error("新增机器人评论异常", e);
            throw e;
        }
    }

    public void deleteRoboltComment(RobotComment robotComment) {
        try {
            logger.info("删除机器人评论");
            robotCommentMapper.updateByCommentById(robotComment);
        } catch (Exception e) {
            logger.error("删除机器人评论异常", e);
            throw e;
        }
    }

    /**
     * 根据id查询机器人评论
     *
     * @param id
     * @return
     */
    public RobotComment queryCommentById(Integer id) {
        try {
            logger.info("根据id查询机器人评论");
            return robotCommentMapper.queryCommentById(id);
        } catch (Exception e) {
            logger.error("根据id查询机器人评论异常", e);
            throw e;
        }
    }

    /**
     * 更新机器人评论
     *
     * @param comment
     */
    public void updateRoboltComent(RobotComment comment) {
        try {
            logger.info("更新机器人评论");
            robotCommentMapper.updateByPrimaryKeySelective(comment);
        } catch (Exception e) {
            logger.error("更新机器人评论异常", e);
            throw e;
        }
    }

    public void updateRoboltComentByTypeIsNull(RobotComment comment) {
        try {
            logger.info("修改没有类型的评论");
            robotCommentMapper.updateRoboltComentByTypeIsNull(comment);
        } catch (Exception e) {
            logger.error("修改没有类型的评论异常", e);
            throw e;
        }
    }

    /**
     * 根据id查询机器人评论内容
     *
     * @return
     */
    public List<RobotComment> queryRoboltComment(Map map) {
        try {
            logger.info("根据id查询机器人评论内容");
            return robotCommentMapper.queryRoboltComment(map);
        } catch (Exception e) {
            logger.error("根据id查询机器人内容异常", e);
            throw e;
        }
    }
}
