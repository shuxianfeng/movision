package com.movision.utils.postListTest;

import com.movision.test.SpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/6/27 10:23
 */
public class PostListRemoveSameTest extends SpringTestCase {

    @Autowired
    private PostListRemoveSame postListRemoveSame;

    @Test
    public void getList() throws Exception {
        postListRemoveSame.getList();
    }

}