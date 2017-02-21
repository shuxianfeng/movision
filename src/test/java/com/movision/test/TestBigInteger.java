package com.movision.test;

import org.junit.Test;

import java.math.BigInteger;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/17 16:41
 */
public class TestBigInteger extends SpringTestCase {

    /**
     * 6
     * true
     * true
     * false
     */
    @Test
    public void test() {
        //初始
        BigInteger num = new BigInteger("0");
        num = num.setBit(2);
        num = num.setBit(1);
        System.out.println(num);
        System.out.println(num.testBit(2));
        System.out.println(num.testBit(1));
        System.out.println(num.testBit(3));
    }


}
