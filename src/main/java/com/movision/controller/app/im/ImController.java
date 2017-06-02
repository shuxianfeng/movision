package com.movision.controller.app.im;

import com.movision.common.Response;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.im.ImFacade;
import com.movision.facade.newInformation.NewInformationsFacade;
import com.movision.mybatis.imFirstDialogue.entity.ImFirstDialogue;
import com.movision.mybatis.imFirstDialogue.entity.ImMsg;
import com.movision.mybatis.newInformation.entity.NewInformation;
import com.movision.utils.ListUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private NewInformationsFacade newInformationsFacade;

    /**
     * 打招呼接口
     *
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "打招呼", notes = "打招呼", response = Response.class)
    @RequestMapping(value = "/say_hi", method = RequestMethod.POST)
    public Response sayHi(@ApiParam @ModelAttribute ImMsg imMsg) throws IOException {
        return imFacade.doFirstCommunicate(imMsg, 2, "打招呼");
    }

    /**
     * 回复打招呼
     *
     * @param imMsg
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "回复打招呼", notes = "回复打招呼", response = Response.class)
    @RequestMapping(value = "/reply_say_hi", method = RequestMethod.POST)
    public Response replySayHi(@ApiParam @ModelAttribute ImMsg imMsg) throws IOException {
        return imFacade.doFirstCommunicate(imMsg, 3, "回复打招呼");
    }


    /**
     * 判断当前用户和对方是什么情况
     * is_say_hi=true, is_reply=true, 表示两人已经是好友，并且可以对话 (对话调IM接口)
     * is_say_hi=true, is_reply=false, 表示对方还未回复，此时我不能再私信给对方 （提示）
     * is_say_hi=false, is_reply=false, 表示两人都没有打招呼  （可以打招呼）
     * is_say_hi=false, is_reply=true, 表示对方向我打招呼，但，我还没有回复 （可以回复打招呼）
     *
     * @param toAccid
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "查询是否有权限发送消息", notes = "查询是否有权限发送消息", response = Response.class)
    @RequestMapping(value = "/get_communication_situation", method = RequestMethod.POST)
    public Response getCommunicationSituation(@ApiParam("被发消息的人的accid") @RequestParam String toAccid) throws IOException {

        Response response = new Response();
        String accid = ShiroUtil.getAccid();
        //默认是双方未互打招呼
        Boolean is_say_hi = false;
        Boolean is_reply = false;
        Map map = new HashedMap();

        List<ImFirstDialogue> list = imFacade.selectFirstDialog(toAccid);
        if (ListUtil.isNotEmpty(list)) {
            if (list.size() > 1) {
                //说明用户已经打过招呼了，并且对方已经回复
                is_say_hi = true;
                is_reply = true;
            } else {
                if (list.get(0).getFromid().equals(accid)) {
                    //说明用户已经打过招呼，但对方还未回复
                    is_say_hi = true;
                } else {
                    //说明是对方先打招呼，用户还未回复
                    is_reply = true;
                }
            }
        }
        map.put("is_say_hi", is_say_hi);
        map.put("is_reply", is_reply);
        response.setData(map);
        return response;
    }

    @ApiOperation(value = "更最新消息已读", notes = "更新用户最新消息为已读", response = Response.class)
    @RequestMapping(value = "/update_new_informations", method = RequestMethod.POST)
    public Response UpdateNewInformations(@ApiParam(value = "登录用户id") @RequestParam String userid) {
        Response response = new Response();
        newInformationsFacade.updateNewInformtions(Integer.parseInt(userid));
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        return response;
    }


    @ApiOperation(value = "查询用户最新消息", notes = "查询用户最新消息", response = Response.class)
    @RequestMapping(value = "/query_new_informations", method = RequestMethod.POST)
    public Response QueryNewInformations(@ApiParam(value = "登录用户id") @RequestParam String userid) {
        Response response = new Response();
        NewInformation res = newInformationsFacade.queryNewInformations(Integer.parseInt(userid));
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(res);
        return response;
    }

}
