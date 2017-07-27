package com.movision.mybatis.user.service;

import com.movision.mybatis.user.entity.User;
import com.movision.test.SpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @Author zhuangyuhao
 * @Date 2017/7/27 10:31
 */
public class UserServiceTest extends SpringTestCase {

    @Autowired
    private UserService userService;

    @Test
    public void testselectByPrimaryKey() throws Exception {
        User user = userService.selectByPrimaryKey(326);
        System.out.println("获取的用户实体：" + user.toString());
        System.out.println("热度：" + user.getHeat_value());
    }

}