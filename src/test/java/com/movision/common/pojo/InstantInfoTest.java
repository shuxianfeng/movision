package com.movision.common.pojo;

import com.movision.test.SpringTestCase;
import com.movision.utils.DateUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/8/2 14:52
 */
public class InstantInfoTest extends SpringTestCase {

    @Test
    public void testCompare() throws Exception {
        List<InstantInfo> list = new ArrayList<>();

        InstantInfo i1 = new InstantInfo();
        i1.setIntime(new Date());
        i1.setObject("lalalal");

        list.add(i1);

        InstantInfo i2 = new InstantInfo();
        i2.setIntime(DateUtils.str2Date("2017-08-01"));
        i2.setObject("adadadad");

        list.add(i2);

        InstantInfo i3 = new InstantInfo();
        i3.setIntime(DateUtils.str2Date("2017-08-03"));
        i3.setObject("xxxxxx");

        list.add(i3);

        System.out.println("排序前：" + list.toString());
        Collections.sort(list, InstantInfo.intimeComparator);
        System.out.println("排序后：" + list.toString());


    }

}