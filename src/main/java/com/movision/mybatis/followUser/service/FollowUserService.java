package com.movision.mybatis.followUser.service;

import com.movision.mybatis.followUser.entity.FollowUser;
import com.movision.mybatis.followUser.entity.FollowUserVo;
import com.movision.mybatis.followUser.mapper.FollowUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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


    public int cancleFollowUser(Map followUser) {
        try {
            log.info("取消关注用户");
            return followUserMapper.cancleFollowUser(followUser);
        } catch (Exception e) {
            log.error("取消关注用户失败", e);
            throw e;
        }
    }

    public int insertUserFans(int interestedusers) {
        try {
            log.info("用户粉丝数加1");
            return followUserMapper.insertUserFans(interestedusers);
        } catch (Exception e) {
            log.error("用户粉丝数加1失败", e);
            throw e;
        }
    }

    public List<FollowUserVo> selectFollowUserVoList(Integer userid) {
        try {
            log.info("查询关注我的人的列表");
            return followUserMapper.selectFollowUserVoList(userid);
        } catch (Exception e) {
            log.error("查询关注我的人的列表失败", e);
            throw e;
        }
    }

    public int updateFollowuserVo(FollowUserVo followUserVo) {
        try {
            log.info("更新关注用户表数据");
            return followUserMapper.updateFollowuserVo(followUserVo);
        } catch (Exception e) {
            log.error("更新关注用户表数据失败", e);
            throw e;
        }

    }



}
