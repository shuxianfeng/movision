package com.movision.mybatis.userRefreshRecord.entity;

import com.movision.test.SpringTestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/8/1 12:51
 */
public class UserReflushCountComparatorTest extends SpringTestCase {
    @Test
    public void compare() throws Exception {

        List<UesrreflushCount> list = new ArrayList<>();
        UesrreflushCount u1 = new UesrreflushCount(1, 4);
        UesrreflushCount u2 = new UesrreflushCount(2, 3);
        UesrreflushCount u3 = new UesrreflushCount(3, 6);

        list.add(u1);
        list.add(u2);
        list.add(u3);

        System.out.println("排序前的值：" + list.toString());
        System.out.println("排序前的值");
        for (int i = 0; i < list.size(); i++) {
            UesrreflushCount emp = list.get(i);
            System.out.println(emp.getCount());
        }

        Collections.sort(list, UesrreflushCount.countComparator);

        System.out.println("排序后的值：" + list.toString());
        System.out.println("排序后的值");
        for (int i = 0; i < list.size(); i++) {
            UesrreflushCount emp = list.get(i);
            System.out.println(emp.getCount());
        }

    }

}