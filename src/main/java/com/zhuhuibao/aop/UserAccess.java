package com.zhuhuibao.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jianglz
 * @since 16/7/5.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAccess {

    //ADMIN: 管理员       ALL:所有用户
    String value() default "ADMIN";

    //30:个人黄金 60:个人铂金 130:企业黄金 160:企业铂金
    String viplevel();// default "30,60,130,160";
}
