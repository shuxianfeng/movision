package com.movision.facade.robot;

import com.movision.mybatis.user.service.UserService;
import com.movision.test.SpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

/**
 * @Author zhuangyuhao
 * @Date 2017/9/18 17:12
 */
public class RobotFacadeTest extends SpringTestCase {
    @Autowired
    private RobotFacade robotFacade;
    @Autowired
    private UserService userService;

    @Test
    public void doZanAction() throws Exception {
        Random random = new Random();
        //随机获取0-16之间的一个随机整数
        for (int i = 0; i < 10; i++) {
            /**
             *
             ====结果：59
             ====结果：16
             ====结果：2
             ====结果：73
             ====结果：9
             ====结果：54
             ====结果：78
             ====结果：39
             ====结果：1
             ====结果：8
             */
            System.out.println("====结果：" + random.nextInt(100));
        }
    }

    @Test
    public void batchChangeRobotPhoto() throws Exception {
//        List<User> userList = userService.selectRobotUser();
        robotFacade.batchChangeRobotPhoto("10001,10002");
    }

    @Test
    public void batchChangeRobotNickname() throws Exception {
//        List<User> userList = userService.selectRobotUser();
        robotFacade.batchChangeRobotNickname("10001,10002");
    }

}