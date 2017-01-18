package com.movision.mybatis.post.service;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/1/18 14:27
 */
@Service
public class PostService {

    @Autowired
    private PostMapper postMapper;

    public List<PostVo> queryTodayEssence() {
        return postMapper.queryTodayEssence();
    }

    public List<PostVo> queryDayAgoEssence(int dayago) {
        return postMapper.queryDayAgoEssence(dayago);
    }

    public List<Circle> queryMayLikeCircle(int userid) {
        return postMapper.queryMayLikeCircle(userid);
    }

    public List<Circle> queryRecommendCircle() {
        return postMapper.queryRecommendCircle();
    }
}
