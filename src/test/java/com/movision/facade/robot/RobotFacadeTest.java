package com.movision.facade.robot;

import com.movision.test.SpringTestCase;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/9/18 17:12
 */
public class RobotFacadeTest extends SpringTestCase {
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



}