package com.movision.controller.app2;

import com.movision.common.Response;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.user.entity.UserMineInfo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author shuxf
 * @Date 2017/7/24 15:15
 * 美番2.0版本“我的”板块中所有接口
 */
@RestController
@RequestMapping("/app/mine2/")
public class AppMineController {

    @Autowired
    private UserFacade userFacade;

    @ApiOperation(value = "我的页面整体信息接口（上半部分）", notes = "含：头像、昵称、关注数、粉丝数、被赞数、被收藏数、帖子数、活动数、收藏数", response = Response.class)
    @RequestMapping(value = "mineInfoUp", method = RequestMethod.POST)
    public Response getMineInfoUp(@ApiParam(value = "用户id(必填，否则无法进入‘我的’页面)") @RequestParam String userid){
        Response response = new Response();

//        UserMineInfo userinfo = userFacade.getMineInfoUp(userid);
//        if (response.getCode() == 200) {
//            response.setMessage("查询成功");
//        }
//        response.setData(userinfo);
        return response;
    }
}
