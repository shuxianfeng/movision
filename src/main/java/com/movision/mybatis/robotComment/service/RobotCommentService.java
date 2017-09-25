package com.movision.mybatis.robotComment.service;

import com.movision.mybatis.robotComment.entity.RobotComment;
import com.movision.mybatis.robotComment.mapper.RobotCommentMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * @param type
     * @param pag
     * @return
     */
    public List<RobotComment> findAllQueryRoboltComment(Integer type, Paging<RobotComment> pag) {
        try {
            logger.info("查询机器人评论列表");
            return robotCommentMapper.findAllQueryRoboltComment(type, pag.getRowBounds());
        } catch (Exception e) {
            logger.error("查询机器人评论列表", e);
            throw e;
        }
    }

    public void insertRoboltComment(RobotComment comment) {
        try {
            logger.info("新增机器人评论");
            robotCommentMapper.insertSelective(comment);
        } catch (Exception e) {
            logger.error("新增机器人评论异常", e);
            throw e;
        }
    }

    public void deleteRoboltComment(Integer id) {
        try {
            logger.info("删除机器人评论");
            robotCommentMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("删除机器人评论异常", e);
            throw e;
        }
    }
}
