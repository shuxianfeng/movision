package com.movision.mybatis.userBehavior.service;

import com.movision.mybatis.userBehavior.entity.UserBehavior;
import com.movision.mybatis.userBehavior.mapper.UserBehaviorMapper;
import com.movision.mybatis.userPhoto.service.UserPhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhanglei
 * @Date 2017/10/27 10:47
 */
@Service
public class UserBehaviorService {

    private static Logger log = LoggerFactory.getLogger(UserBehaviorService.class);
    @Autowired
    private UserBehaviorMapper userBehaviorMapper;


    /*
    查询用户分析表
     */
    public List<UserBehavior> findAllUserBehavior(int userid) {
        try {
            log.info("查询用户分析表");
            return userBehaviorMapper.findAllUserBehavior(userid);
        } catch (Exception e) {
            log.error("查询用户分析表失败", e);
            throw e;
        }
    }
}
