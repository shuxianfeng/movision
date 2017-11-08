package com.movision.controller.app.im;

import com.movision.common.Response;
import com.movision.facade.im.ImFacade;
import com.movision.mybatis.imFirstDialogue.entity.ImMsg;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/7 19:16
 */
@RestController
@RequestMapping("/app/im")
public class ImController {

    private static Logger log = LoggerFactory.getLogger(ImController.class);

    @Autowired
    private ImFacade imFacade;

    /**
     * 打招呼接口
     *
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "发消息", notes = "发消息", response = Response.class)
    @RequestMapping(value = "/send_msg", method = RequestMethod.POST)
    public Response sayHi(@ApiParam @ModelAttribute ImMsg imMsg) throws IOException {
        Response response = new Response();
        imFacade.sendImMsg(imMsg);
        return response;
    }


    @ApiOperation(value = "更新用户最新消息为已读", notes = "更新用户最新消息为已读", response = Response.class)
    @RequestMapping(value = "/update_new_informations", method = RequestMethod.POST)
    public Response UpdateNewInformations(@ApiParam(value = "登录用户id") @RequestParam String userid) {
        Response response = new Response();
        // TODO: 2017/9/19 使用mongo记录
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        return response;
    }


    @ApiOperation(value = "查询用户最新消息", notes = "查询用户最新消息", response = Response.class)
    @RequestMapping(value = "/query_new_informations", method = RequestMethod.POST)
    public Response QueryNewInformations(@ApiParam(value = "登录用户id") @RequestParam String userid) {
        Response response = new Response();
        // TODO: 2017/9/19 使用mongo记录
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }

        return response;
    }

}
