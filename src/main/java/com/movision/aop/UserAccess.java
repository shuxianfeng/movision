package com.movision.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhuangyuhao
 * @since 16/7/5.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAccess {

    //ADMIN: 管理员       ALL:所有用户
    String value() default "ADMIN";

    //用户等级：0 普通用户  1 青铜  2 白银 3 黄金 4 白金 5 钻石 6 金钻石 7皇冠 8金皇冠
    String level();
}
