package com.zhuhuibao.test;

import com.google.gson.Gson;
//import com.zhuhuibao.mybatis.memCenter.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jianglz
 * @since 14/11/7.
 */
public class Test extends BaseSpringContext {


    @org.junit.Test
    public void testConstants() throws Exception {


    }

/*    public static void main(String[] args) {
//        for (int i = 0;i<10;i++) {
//            if(i > 3)  break;
//
//            System.out.println(i);
//        }

//        String s = "fs:/attachment/goods/201411121427199489.jpg";
//        s = s.replace("fs:","http://122.194.5.221/statics");
//        System.out.println(s);
//        Date d = new Date();
//
//        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//12小时制
//
//        System.out.println(ss.format(d));
//
//        Date date = new Date();
//
//        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
//
//        String LgTime = sdformat.format(date);
//
//        System.out.println(LgTime);
//        String s = "localhost:8080/index";
//        int index = s.lastIndexOf("/");
//        System.out.print(index);
        User user = new User();
        user.setId(1);
        user.setMobile("112313");
        List<User> list = new ArrayList<User>();
        list.add(user);
        Gson gson = new Gson();
        System.out.println(gson.toJson(list));
    }*/


}
