package com.movision.controller.app.wechat;

import com.movision.common.Response;
import com.movision.facade.wechat.WechatFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author shuxf
 * @Date 2018/2/1 16:42
 */
@RestController
@RequestMapping("/app/wechat/")
public class WxLoginController {

    @Autowired
    private WechatFacade wechatFacade;

    /**
     * 微信小程序：使用登录凭证 code 获取 session_key 和 openid
     */
    @ApiOperation(value = "code 换 openid", notes = "code 换 openid", response = Response.class)
    @RequestMapping(value = "getOpenid", method = RequestMethod.POST)
    public Response getOpenid(@ApiParam(value = "登录时H5获取的code") @RequestParam String code){

        Response response  = wechatFacade.getOpenid(code);

        if (response.getCode()==200){
            response.setMessage("获取成功");
        }else {
            response.setMessage("获取失败");
        }
        return response;
    }
}
