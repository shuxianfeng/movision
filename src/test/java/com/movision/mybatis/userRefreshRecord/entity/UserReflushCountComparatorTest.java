package com.movision.mybatis.userRefreshRecord.entity;

import com.movision.test.SpringTestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/8/1 12:51
 */
public class UserReflushCountComparatorTest extends SpringTestCase {
    @Test
    public void compare() throws Exception {

        List<UserReflushCount> list = new ArrayList<>();
        UserReflushCount u1 = new UserReflushCount(1, 4);
        UserReflushCount u2 = new UserReflushCount(2, 3);
        UserReflushCount u3 = new UserReflushCount(3, 6);

        list.add(u1);
        list.add(u2);
        list.add(u3);

        System.out.println("排序前的值：" + list.toString());
        System.out.println("排序前的值");
        for (int i = 0; i < list.size(); i++) {
            UserReflushCount emp = list.get(i);
            System.out.println(emp.getCount());
        }

        Collections.sort(list, UserReflushCount.countComparator);

        System.out.println("排序后的值：" + list.toString());
        System.out.println("排序后的值");
        for (int i = 0; i < list.size(); i++) {
            UserReflushCount emp = list.get(i);
            System.out.println(emp.getCount());
        }

    }

}