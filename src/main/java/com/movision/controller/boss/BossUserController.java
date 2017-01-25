package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.user.BossUserFacade;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.security.EncodeUtil;
import com.movision.utils.MD5Util;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/24 18:06
 */
@RestController
@RequestMapping("boss/user")
public class BossUserController {

    @Autowired
    private BossUserFacade bossUserFacade;

    @RequestMapping(value = "add_boss_user", method = RequestMethod.POST)
    @ApiOperation(value = "新增boss用户", notes = "新增boss用户", response = Response.class)
    public Response addBossUser(@ApiParam @ModelAttribute BossUser bossUser) throws UnsupportedEncodingException {
        Response response = new Response();
        //密码加密
        String pwd = new Md5Hash(bossUser.getPassword(), null, 2).toString();

        bossUser.setPassword(pwd);

        boolean flag = bossUserFacade.addUser(bossUser);
        if (flag) {
            response.setCode(200);
        } else {
            response.setCode(400);
        }
        return response;
    }

    @RequestMapping(value = "update_boss_user", method = RequestMethod.POST)
    @ApiOperation(value = "修改boss用户", notes = "修改boss用户", response = Response.class)
    public Response updateBossUser(@ApiParam @ModelAttribute BossUser bossUser) throws UnsupportedEncodingException {
        Response response = new Response();
        //修改密码
        String password = bossUser.getPassword();
        if (StringUtils.isNotEmpty(password)) {
            String pwd = new Md5Hash(password, null, 2).toString();

            bossUser.setPassword(pwd);
        }

        boolean flag = bossUserFacade.updateUser(bossUser);
        if (flag) {
            response.setCode(200);
        } else {
            response.setCode(400);
        }
        return response;
    }

}
