package com.zhuhuibao.mobile.web.mc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhuhuibao.common.constant.MessageConstant;
import com.zhuhuibao.mybatis.memCenter.entity.Message;
import com.zhuhuibao.service.MobileMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.service.MobileMessageService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 消息controller（系统信息、留言信息）
 * 
 * @author tongxinglong
 * @since 2016/10/12
 */
@RestController
@RequestMapping("/rest/m/message/mc")
public class MobileMessageController {

    @Autowired
    private MobileMessageService messageService;


    @ApiOperation(value = "获取系统消息列表", notes = "获取系统消息列表", response = Response.class)
    @RequestMapping(value = "/sel_sysmsg_list", method = RequestMethod.GET)
    public Response selSysMsgList(@ApiParam(value = "状态：1：未读，2：已读") @RequestParam(required = false) String status, @RequestParam(required = false, defaultValue = "1") String pageNo,
            @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        Response response = new Response();

        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        Long memberId = ShiroUtil.getCreateID();
        if (memberId != null) {
            map.put("recID", String.valueOf(memberId));
            List<Map<String, String>> list = messageService.getSysMsgList(pager, map);
            pager.result(list);
            response.setData(pager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "查看消息详情", notes = "查看消息详情", response = Response.class)
    @RequestMapping(value = "/sel_sysmsg_detail", method = RequestMethod.GET)
    public Response selSysMsgDetail(@RequestParam String id) {
        Response response = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if (memberId != null) {
            Map<String, String> map = messageService.getSysMsgDetail(memberId, id);
            response.setData(map);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "我收到的留言列表", notes = "我收到的留言列表", response = Response.class)
    @RequestMapping(value = "/sel_receivemsg_list", method = RequestMethod.GET)
    public Response selReceiveMsgList(@RequestParam(required = false, defaultValue = "1") String pageNo, @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();

        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Long memberId = ShiroUtil.getCreateID();
        if (memberId != null) {
            List<Map<String, String>> list = messageService.getReceiveMsgList(pager, memberId);
            pager.result(list);
            response.setData(pager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "查看我收到的留言详情", notes = "查看我收到的留言详情", response = Response.class)
    @RequestMapping(value = "/sel_receivemsg_detail", method = RequestMethod.GET)
    public Response selReceiveMsgDetail(@RequestParam String id) {
        Response response = new Response();
        Map<String, String> map = messageService.getReceiveMsgDetail(id);
        response.setData(map);
        return response;
    }
}
