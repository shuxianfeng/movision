package com.movision.controller.boss.im;

import com.movision.common.Response;
import com.movision.facade.im.ImFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/9 10:46
 */
@RestController
@RequestMapping("boss/sys/im")
public class BossImController {

    @Autowired
    private ImFacade imFacade;

    /**
     * 该接口给系统管理员调用
     *
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "发送系统通知", notes = "发送系统通知", response = Response.class)
    @RequestMapping(value = {"/send_system_inform"}, method = RequestMethod.POST)
    public Response sendSystemInform(@ApiParam(value = "系统通知内容") @RequestParam String body) throws IOException {
        Response response = new Response();

        imFacade.sendSystemInform(body);
        return response;
    }


}
