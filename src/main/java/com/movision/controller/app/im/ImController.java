package com.movision.controller.app.im;

import com.movision.common.Response;
import com.movision.facade.im.ImFacade;
import com.movision.mybatis.imFirstDialogue.entity.ImMsg;
import com.movision.mybatis.imUserAccusation.entity.ImUserAccusation;
import com.movision.utils.ListUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

    @ApiOperation(value = "举报im用户", notes = "举报im用户", response = Response.class)
    @RequestMapping(value = "/add_im_user_accusation_record", method = RequestMethod.POST)
    public Response addImUserAccusationRecord(@ApiParam(value = "举报人accid") @RequestParam Integer from,
                                              @ApiParam(value = "被举报人accid") @RequestParam Integer to,
                                              @ApiParam(value = "举报内容") @RequestParam String content) {
        Response response = new Response();
        List<ImUserAccusation> list = imFacade.queryNotHandleSelectiveRecord(from, to, 0);
        if (ListUtil.isEmpty(list)) {
            imFacade.addImUserAccusationRecord(from, to, content);
        }
        response.setMessage("举报成功");
        return response;
    }




}
