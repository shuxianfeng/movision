package com.movision.facade.boss;

import com.movision.mybatis.post.entity.Post;
import com.movision.test.SpringTestCase;
import com.movision.utils.pagination.model.Paging;
import javafx.geometry.Pos;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/27 11:31
 */
public class PostFacadeTest extends SpringTestCase {
    @Autowired
    private PostFacade postFacade;

    @Test
    public void findAllMyCollectPostList() throws Exception {
        Paging<Post> postPaging = new Paging<Post>(1, 10);
        List<Post> postList = postFacade.findAllMyCollectPostList(postPaging, 1);
        postPaging.result(postList);
        System.out.println(postPaging);
    }

}