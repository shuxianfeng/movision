package com.movision.test;

import com.movision.facade.user.UserFacade;
import com.movision.mybatis.user.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/6 14:37
 */
public class RedisTest extends SpringTestCase {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserFacade userFacade;

    @Test
    public void redisTest() throws InterruptedException {

        User user = new User();
        user.setId(10);
        user.setPhone("18051989558");
        user.setNickname("zyh");
        System.out.println("=======addUser===================");
        userFacade.addUser(user);


        System.out.println("=======第一次getUserByID============");
        System.out.println(userFacade.getUserByID(11));
        System.out.println("=======第二次getUserByID============");
        System.out.println(userFacade.getUserByID(11));
        System.out.println("===============================");

//        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
//        System.out.println("set前：" + opsForValue.get("user"));
//        opsForValue.set("user", user);
//        System.out.println("set后：" + opsForValue.get("user"));
//        redisTemplate.delete("user");
//        System.out.println("delete后：" + opsForValue.get("user"));


    }

}
