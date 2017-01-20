package com.movision.mybatis.post.service;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public List<Post> queryHotActiveList() {
        return postMapper.queryHotActiveList();
    }

    public List<Post> queryCircleSubPost(int circleid) {
        return postMapper.queryCircleSubPost(circleid);
    }

    public PostVo queryPostDetail(int postid) {
        return postMapper.queryPostDetail(postid);
    }

    public List<PostVo> queryPastPostList(Map<String, Object> parammap) {
        return postMapper.queryPastPostList(parammap);
    }
}
