package com.movision.mybatis.followCircle.service;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.followCircle.entity.FollowCircle;
import com.movision.mybatis.followCircle.mapper.FollowCircleMapper;
import com.movision.mybatis.post.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/23 10:31
 */
@Service
@Transactional
public class FollowCircleService {

    private static Logger log = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private FollowCircleMapper followCircleMapper;

    public int isFollow(Map<String, Object> parammap) {
        try {
            log.info("查询该用户是否已关注该圈子");
            return followCircleMapper.queryCountByFollow(parammap);

        } catch (Exception e) {
            log.error("查询该用户是否已关注该圈子失败");
            throw e;

        }
    }

    public    List<Circle> queryUserFollowCircle(int userid){
        try {
            log.info("查询用户关注的圈子");
            return followCircleMapper.queryUserFollowCircle(userid);
        }catch (Exception e){
            log.error("查询用户关注的圈子失败");
            throw e;
        }
    }

}
