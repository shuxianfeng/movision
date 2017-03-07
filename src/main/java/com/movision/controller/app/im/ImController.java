package com.movision.controller.app.im;

import com.movision.common.Response;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.im.ImFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/7 19:16
 */
@RestController
@RequestMapping("app/im")
public class ImController {

    @Autowired
    private ImFacade imFacade;

    @ApiOperation(value = "加好友", notes = "加好友", response = Response.class)
    @RequestMapping(value = {"/add_im_friend"}, method = RequestMethod.POST)
    public Response testGetAppUserInfo(@ApiParam(value = "加好友接收者accid") @RequestParam String faccid,
                                       @ApiParam(value = "1直接加好友，2请求加好友，3同意加好友，4拒绝加好友") @RequestParam int type,
                                       @ApiParam(value = "加好友对应的请求消息，最长256字符") @RequestParam(required = false) String msg) throws IOException {
        Response response = new Response();

        Map map = imFacade.addFriend(ShiroUtil.getAccid(), faccid, type, msg);
        if (map.get("code").equals(200)) {
            response.setCode(200);
            response.setMessage("添加好友成功");
        } else {
            response.setCode(400);
            response.setMessage("添加好友失败");
        }
        return response;
    }


}
