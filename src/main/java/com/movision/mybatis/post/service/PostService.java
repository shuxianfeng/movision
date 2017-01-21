package com.movision.mybatis.post.service;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.mapper.PostMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger log = LoggerFactory.getLogger(PostService.class);

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

    public int queryPostNumByCircleid(int circleid) {
        try {
            log.info("查询圈子中更新的帖子数");
            return postMapper.queryPostNumByCircleid(circleid);
        } catch (Exception e) {
            log.error("查询圈子中更新的帖子数失败");
            throw e;
        }
    }

    public PostVo queryPostDetail(int postid) {
        return postMapper.queryPostDetail(postid);
    }

    public List<PostVo> queryPastPostList(Map<String, Object> parammap) {
        return postMapper.queryPastPostList(parammap);
    }

    public List<PostVo> queryPostList(Paging<Post> pager, String circleid) {
        try {
            log.info("查询某个圈子发出的所有帖子列表");
            return postMapper.queryPostList(pager.getRowBounds(), Integer.parseInt(circleid));
        } catch (Exception e) {
            log.error("查询帖子列表失败");
            throw e;
        }
    }
}
