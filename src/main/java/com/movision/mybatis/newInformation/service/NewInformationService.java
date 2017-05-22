package com.movision.mybatis.newInformation.service;

import com.movision.mybatis.newInformation.entity.NewInformation;
import com.movision.mybatis.newInformation.mapper.NewInformationMapper;
import org.apache.regexp.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/5/19 13:41
 */
@Service
public class NewInformationService {

    private static Logger logger = LoggerFactory.getLogger(NewInformationService.class);

    @Autowired
    private NewInformationMapper newInformationMapper;

    /**
     * 查询用户是否有最新消息
     *
     * @param postid
     * @return
     */
    public Integer queryUserByNewInformation(Integer postid) {
        try {
            logger.info("查询用户是否有最新消息");
            return newInformationMapper.queryUserByNewInformation(postid);
        } catch (Exception e) {
            logger.error("查询用户是否有最新消息异常", e);
            throw e;
        }
    }

    /**
     * 更新用户最新消息记录
     *
     * @param news
     */
    public void updateUserByNewInformation(NewInformation news) {
        try {
            logger.info("更新用户最新消息记录");
            newInformationMapper.updateUserByNewInformation(news);
        } catch (Exception e) {
            logger.error("更新用户最新消息记录异常", e);
            throw e;
        }
    }

    /**
     * 新增用户最新消息记录
     *
     * @param news
     */
    public void insertUserByNewInformation(NewInformation news) {
        try {
            logger.info("新增用户最新消息记录");
            newInformationMapper.insertUserByNewInformation(news);
        } catch (Exception e) {
            logger.error("新增用户最新消息记录异常", e);
            throw e;
        }
    }


    /**
     * 查询被点赞人的帖子评论是否被设为最新消息通知用户
     *
     * @param commentid
     * @return
     */
    public Integer queryUserByNewInformationByCommentid(Integer commentid) {
        try {
            logger.info("查询被点赞人的帖子评论是否被设为最新消息通知用户");
            return newInformationMapper.queryUserByNewInformationByCommentid(commentid);
        } catch (Exception e) {
            logger.error("查询被点赞人的帖子评论是否被设为最新消息通知用户异常", e);
            throw e;
        }
    }

    /**
     * 查询用户是否有最新系统通知消息
     *
     * @param accid
     * @return
     */
    public Integer querySystemByNewInformation(String accid) {
        try {
            logger.info("查询用户是否有最新系统通知消息");
            return newInformationMapper.querySystemByNewInformation(accid);
        } catch (Exception e) {
            logger.error("查询用户是否有最新系统通知消息", e);
            throw e;
        }
    }

    /**
     * 查询用户打招呼的最新通知
     *
     * @param toaccid
     * @return
     */
    public Integer queryCollByNewInformation(String toaccid) {
        try {
            logger.info("查询用户打招呼的最新通知");
            return newInformationMapper.queryCollByNewInformation(toaccid);
        } catch (Exception e) {
            logger.error("查询用户打招呼的最新通知异常", e);
            throw e;
        }
    }

    /**
     * 更新用户最新消息
     *
     * @param map
     * @return
     */
    public void updateNewInformtions(Map map) {
        try {
            logger.info("更新用户最新消息");
            newInformationMapper.updateNewInformtions(map);
        } catch (Exception e) {
            logger.error("更新用户最新消息异常", e);
            throw e;
        }
    }

    /**
     * 查询用户最新消息记录
     *
     * @param userid
     * @return
     */
    public NewInformation queryNewInformationByUserid(Integer userid) {
        try {
            logger.info("查询用户最新消息");
            return newInformationMapper.queryNewInformationByUserid(userid);
        } catch (Exception e) {
            logger.error("查询用户最新消息异常", e);
            throw e;
        }
    }
}
