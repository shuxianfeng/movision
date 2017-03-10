package com.movision.common.constant;

import com.movision.utils.propertiesLoader.BossPropertiesLoader;

import java.util.Date;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/18 14:59
 */
public class UserConstants {

    /**
     * 超管角色id
     */
    public static final String SUPER_ADMIN = BossPropertiesLoader.getValue("boss_superadmin_role");
    /**
     * 系统管理员角色id
     */
    public static final String SYSTEM_ADMIN = BossPropertiesLoader.getValue("boss_system_admin_role");



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
