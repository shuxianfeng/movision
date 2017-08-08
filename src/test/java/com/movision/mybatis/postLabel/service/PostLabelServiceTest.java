package com.movision.mybatis.postLabel.service;

import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.test.SpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/7/25 16:52
 */
public class PostLabelServiceTest extends SpringTestCase {

    @Autowired
    private PostLabelService postLabelService;

    @Test
    public void queryLabelIdList() throws Exception {
        String[] strings = {"活动", "风景"};
        List<Integer> list = postLabelService.queryLabelIdList(strings);
        System.out.println("结果：" + list.toString());
    }

    @Test
    public void addOneLabel() throws Exception {
        PostLabel postLabel = new PostLabel();
        postLabel.setName("test1");
        postLabel.setUserid(1);
        postLabel.setType(3);

        postLabelService.insertPostLabel(postLabel);
    }

    @Test
    public void queryCityListByCityname() throws Exception {
        List<PostLabel> labels = postLabelService.queryCityListByCityname("京");
        System.out.println("【结果：】" + labels.toString());
    }

}