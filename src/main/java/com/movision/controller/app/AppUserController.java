package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.user.entity.UserVo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author shuxf
 * @Date 2017/2/6 17:07
 * 个人中心
 */
@RestController
@RequestMapping("/app/user/")
public class AppUserController {

    @Autowired
    private UserFacade userFacade;

    @ApiOperation(value = "个人主页--个人信息", notes = "用于返回个人主页中个人信息的接口", response = Response.class)
    @RequestMapping(value = "personPage", method = RequestMethod.POST)
    public Response personPage(@ApiParam(value = "用户id") @RequestParam String userid) {
        Response response = new Response();

        UserVo userVo = userFacade.queryUserInfo(userid);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(userVo);
        return response;
    }
}
