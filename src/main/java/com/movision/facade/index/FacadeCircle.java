package com.movision.facade.index;

import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/1/20 20:36
 */
@Service
public class FacadeCircle {

    @Autowired
    private CircleService circleService;

    @Autowired
    private PostService postService;

    public CircleVo queryCircleIndex1(int circleid) {

        CircleVo circleVo = circleService.queryCircleIndex1(circleid);//查询圈子详情基础数据

        List<Post> postList = postService.queryCircleSubPost(circleid);//查询这个圈子中被设为热帖的5个帖子

        circleVo.setHotPostList(postList);

        return circleVo;

    }
}
