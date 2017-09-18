package com.movision.facade.robot;

import com.movision.test.SpringTestCase;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/9/18 17:12
 */
public class RobotFacadeTest extends SpringTestCase{
    @Test
    public void doZanAction() throws Exception {
        //随机获取0-16之间的一个随机整数

        System.out.println("====结果：" + new Random().nextInt(16));

    }

}