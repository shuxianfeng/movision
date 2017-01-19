package com.movision.facade.index;

import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author shuxf
 * @Date 2017/1/19 15:43
 */
@Service
public class FacadePost {

    @Autowired
    private PostService postService;

    public PostVo queryPostDetail(int postid) {
        return postService.queryPostDetail(postid);
    }
}
