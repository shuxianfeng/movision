package com.movision.facade.user;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.exception.AuthException;
import com.movision.mybatis.adminUser.entity.AdminUser;
import com.movision.mybatis.adminUser.service.AdminUserService;
import com.movision.mybatis.user.entity.LoginUser;
import com.movision.mybatis.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/17 15:59
 */
@Service
public class UserFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 根据手机获取该用户信息，以及判断是什么角色
     *
     * @param phone
     * @return
     */
    public LoginUser getLoginUserByPhone(String phone) {

        LoginUser loginUser = userService.queryLoginUserByPhone(phone);
        if (null == loginUser) {
            throw new AuthException(MsgCodeConstant.member_mcode_username_not_exist, "该手机号的用户不存在");
        }
        AdminUser adminUser = adminUserService.queryAdminUserByPhone(phone);
        if (null == adminUser) {
            loginUser.setRole("200");   //普通用户
        } else {
            loginUser.setRole("100");   //管理员
        }
        return loginUser;
    }

}
