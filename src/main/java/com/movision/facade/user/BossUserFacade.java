package com.movision.facade.user;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.exception.AuthException;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.bossUser.service.BossUserService;
import com.movision.mybatis.user.entity.LoginUser;
import com.movision.mybatis.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/17 15:59
 */
@Service
public class BossUserFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private BossUserService bossUserService;

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
        BossUser bossUser = bossUserService.queryAdminUserByPhone(phone);
        if (null == bossUser) {
            loginUser.setRole("200");   //普通用户
        } else {
            loginUser.setRole("100");   //管理员
        }
        return loginUser;
    }

    public Boolean addUser(BossUser bossUser) {
        return bossUserService.addUser(bossUser);
    }

    public Boolean updateUser(BossUser bossUser) {
        return bossUserService.updateUser(bossUser);
    }

    public BossUser getByUsername(String username) {
        return bossUserService.getBossUserByUsername(username);
    }

    public Boolean updateLoginInfo(BossUser bossUser) {
        return bossUserService.updateUserLoginInfo(bossUser);
    }

}
