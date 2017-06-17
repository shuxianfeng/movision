package com.movision.mybatis.postDes.service;

import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.postDes.entity.PostDes;
import com.movision.mybatis.postDes.mapper.PostDesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/6/17 11:11
 */
@Service
public class PostDesService {

    private static Logger log = LoggerFactory.getLogger(PostDesService.class);


    @Autowired
    private PostDesMapper postDesMapper;

    public void batchAdd(List<Post> postList) {
        try {
            log.info("批量插入post转化后的数据");
            postDesMapper.batchAdd(postList);
        } catch (Exception e) {
            log.error("批量插入post转化后的数据，失败", e);
            throw e;
        }
    }

    public Integer addFromPost(Post post) {
        try {
            log.info("单个插入yw_post_des");
            return postDesMapper.addFromPost(post);
        } catch (Exception e) {
            log.error("单个插入yw_post_des，失败", e);
            throw e;
        }
    }


}
