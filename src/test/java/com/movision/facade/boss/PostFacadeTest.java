package com.movision.facade.boss;

import com.movision.test.SpringTestCase;
import com.movision.utils.pagination.model.Paging;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/27 11:31
 */
public class PostFacadeTest extends SpringTestCase {
    @Autowired
    private PostFacade postFacade;

    @Test
    public void findAllMyCollectPostList() throws Exception {
        Paging<Map> postPaging = new Paging<Map>(1, 10);
        List<Map> postList = postFacade.findAllMyCollectPostList(postPaging, 1);
        postPaging.result(postList);
        System.out.println(postPaging);
    }

}