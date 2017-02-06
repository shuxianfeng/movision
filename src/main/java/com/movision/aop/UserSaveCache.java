package com.movision.aop;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/6 14:51
 */
@Caching(put = {
        @CachePut(value = "user", key = "'user_id_'+#user.id"),
        @CachePut(value = "user", key = "'user_nickname_'+#user.nickname"),
        @CachePut(value = "user", key = "'user_phone_'+#user.phone")
})
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface UserSaveCache {
}
