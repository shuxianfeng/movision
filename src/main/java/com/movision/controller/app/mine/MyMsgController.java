package com.movision.controller.app.mine;

import com.movision.common.Response;
import com.movision.common.util.ShiroUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * APP我的消息中心
 *
 * @Author zhuangyuhao
 * @Date 2017/4/5 13:46
 */
@RestController
@RequestMapping("app/mine/msg")
public class MyMsgController {

    @ApiOperation(value = "获取我的消息中心列表", notes = "获取我的消息中心列表", response = Response.class)
    @RequestMapping(value = {"/get_my_msg_center_list"}, method = RequestMethod.GET)
    public Response getMyMsgCenterList() {

        Response response = new Response();


        return response;
    }


}
