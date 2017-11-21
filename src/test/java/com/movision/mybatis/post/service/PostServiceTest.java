package com.movision.mybatis.post.service;

import com.movision.mybatis.post.entity.PostVo;
import com.movision.test.SpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/11/21 11:26
 */
public class PostServiceTest extends SpringTestCase {

    @Autowired
    private PostService postService;

    @Test
    public void querySelectedSortedPosts() throws Exception {
        int[] ids = new int[]{2419, 2418, 2420};
        List<PostVo> list = postService.querySelectedSortedPosts(ids);
        for (PostVo p : list) {
            System.out.println(p.getId());
        }
    }

}