package com.movision.mybatis.postLabel.service;

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

}