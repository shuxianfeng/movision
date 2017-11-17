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

    @ApiOperation(value = "发消息", notes = "发消息", response = Response.class)
    @RequestMapping(value = "/send_msg", method = RequestMethod.POST)
    public Response sayHi(@ApiParam @ModelAttribute ImMsg imMsg) throws IOException {
        Response response = new Response();
        imFacade.sendImMsg(imMsg);
        return response;
    }


}
