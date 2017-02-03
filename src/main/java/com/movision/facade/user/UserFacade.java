package com.movision.facade.user;

import com.movision.mybatis.user.entity.RegisterUser;
import com.movision.mybatis.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * APP用户 facade
 * @Author zhuangyuhao
 * @Date 2017/1/24 20:20
 */
@Service
public class UserFacade {

    @Autowired
    private UserService userService;

    public int isExistAccount(String phone) {
        return userService.isExistAccount(phone);
    }

    public int registerAccount(RegisterUser registerUser) {
        return userService.registerAccount(registerUser);
    }
}
