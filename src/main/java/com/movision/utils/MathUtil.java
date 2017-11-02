package com.movision.utils;

import java.math.BigDecimal;

/**
 * 算术工具类
 *
 * @Author zhuangyuhao
 * @Date 2017/3/2 15:58
 */
public class MathUtil {

    /**
     * 先除以100， 再四舍五入取整
     *
     * @param number
     * @return
     */
    public static int division100ToInteger(double number) {
        return new BigDecimal(number / 100).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    /**
     * 取整
     *
     * @param num   基数
     * @param ratio 百分百 0.9
     * @return
     */
    public static int mathCeil(int num, double ratio) {
        return (int) (Math.ceil(num * ratio));
    }

    public static void main(String[] args) {
        /*System.out.println(division100ToInteger(343.58));
        System.out.println(division100ToInteger(353.58));
        System.out.println(division100ToInteger(0.0));*/

//        System.out.println(499 / 500);

        System.out.println(mathCeil(10, 0.05));
        System.out.println(mathCeil(10, 0.9));
        System.out.println(mathCeil(3, 0.05));
        System.out.println(mathCeil(1, 0.9));
    }
}
