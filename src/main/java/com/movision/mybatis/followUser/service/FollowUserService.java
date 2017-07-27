package com.movision.mybatis.followUser.service;

import com.movision.mybatis.followUser.entity.FollowUser;
import com.movision.mybatis.followUser.mapper.FollowUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/7/26 9:53
 */
@Service
public class FollowUserService {
    private static Logger log = LoggerFactory.getLogger(FollowUserService.class);
    @Autowired
    private FollowUserMapper followUserMapper;

    public int insertSelective(FollowUser followUser) {
        try {
            log.info("关注用户");
            return followUserMapper.insertSelective(followUser);
        } catch (Exception e) {
            log.error("关注用户失败", e);
            throw e;
        }
    }

    public int yesOrNo(Map followUser) {
        try {
            log.info("是否关注用户");
            return followUserMapper.yesOrNo(followUser);
        } catch (Exception e) {
            log.error("是否关注用户失败", e);
            throw e;
        }
    }

}
