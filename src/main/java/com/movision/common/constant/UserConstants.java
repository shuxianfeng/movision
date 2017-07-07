package com.movision.common.constant;

import com.movision.utils.propertiesLoader.PropertiesLoader;

import java.util.Date;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/18 14:59
 */
public class UserConstants {

    /**
     * app 用户注册时的默认头像
     */
    public static final String DEFAULT_APPUSER_PHOTO = PropertiesLoader.getValue("default_appuser_photo");


    public enum USER_STATUS {
        normal(0),
        disable(1);

        public final int code;

        USER_STATUS(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String toString() {
            return String.valueOf(this.code);
        }
    }

    public enum USER_IDENTITY {

        ISSUPER("issuper"), //超管
        ISCIRCLE("iscircle"),   //圈主
        CIRCLEMANAGEMENT("circlemanagement"),   //圈子管理员
        CONTRIBUTING("contributing"),   //特约嘉宾
        COMMON("common");   //普通管理员

        public final String name;

        USER_IDENTITY(String name) {
            this.name = name;
        }

        public String getCode() {
            return name;
        }

        @Override
        public String toString() {
            return String.valueOf(this.name);
        }

    }


    public static void main(String[] args) {

        Object param = USER_STATUS.disable.name();
        Object a = USER_STATUS.disable.toString();

        System.out.println(param);  //disable
        System.out.println(a);  //1

        if (param instanceof Integer) {
            int value = ((Integer) param).intValue();
            System.out.println("Integer");
        } else if (param instanceof String) {
            String s = (String) param;
            System.out.println("String");
        } else if (param instanceof Double) {
            double d = ((Double) param).doubleValue();
            System.out.println("Double");
        } else if (param instanceof Float) {
            float f = ((Float) param).floatValue();
            System.out.println("Float");
        } else if (param instanceof Long) {
            long l = ((Long) param).longValue();
            System.out.println("Long");
        } else if (param instanceof Boolean) {
            boolean b = ((Boolean) param).booleanValue();
            System.out.println("Boolean");
        } else if (param instanceof Date) {
            Date d = (Date) param;
            System.out.println("Date");
        } else {
            System.out.println("nothing");
        }
    }


}
