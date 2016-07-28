package com.zhuhuibao.business.mail;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MessageConstant;
import com.zhuhuibao.common.constant.MessageLogConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.Message;
import com.zhuhuibao.mybatis.sitemail.entity.MessageLog;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/6/12 0012.
 */
@RestController
@RequestMapping("/rest/member/mc/mail")
public class MailController {
    private final static Logger log = LoggerFactory.getLogger(MailController.class);

    @Autowired
    SiteMailService siteMailService;

    @ApiOperation(value="消息列表",notes="消息列表",response = Response.class)
    @RequestMapping(value = "sel_newsList", method = RequestMethod.GET)
    public Response findAllNewsList(@ApiParam(value = "状态：1：未读，2：已读")@RequestParam(required = false)String status,
                                    @RequestParam(required = false,defaultValue = "1")String pageNo,
                                    @RequestParam(required = false,defaultValue = "10")String pageSize)  {
        Response response = new Response();

        Paging<Map<String,String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        map.put("status",status);
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            map.put("recID",String.valueOf(memberId));
            siteMailService.addNewsToLog(map);
            List<Map<String,String>> list = siteMailService.findAllNewsList(pager,map);
            pager.result(list);
            response.setData(pager);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value="查看消息详情",notes="查看消息详情",response = Response.class)
    @RequestMapping(value = "sel_news", method = RequestMethod.GET)
    public Response sel_news(@RequestParam String id)  {
        Response response = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            //将这条消息设为已读
            MessageLog messageLog = new MessageLog();
            messageLog.setRecID(memberId);
            messageLog.setMessageID(Long.parseLong(id));
            messageLog.setStatus(MessageLogConstant.NEWS_STATUS_TWO);
            siteMailService.updateStatus(messageLog);

            Map<String,String> map = siteMailService.queryNewsById(id);
            response.setData(map);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value="批量标记为已读",notes="批量标记为已读",response = Response.class)
    @RequestMapping(value = "upd_news", method = RequestMethod.POST)
    public Response upd_news(@ApiParam(value = "消息ids,逗号隔开") @RequestParam String ids)  {
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            MessageLog messageLog = new MessageLog();
            messageLog.setId(Long.parseLong(id));
            messageLog.setStatus(MessageLogConstant.NEWS_STATUS_TWO);
            siteMailService.updateNewsStatus(messageLog);
        }
        return response;
    }

    @ApiOperation(value="批量删除消息",notes="批量删除消息",response = Response.class)
    @RequestMapping(value = "del_news", method = RequestMethod.POST)
    public Response del_news(@ApiParam(value = "消息ids,逗号隔开") @RequestParam String ids)  {
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            MessageLog messageLog = new MessageLog();
            messageLog.setId(Long.parseLong(id));
            messageLog.setStatus(MessageLogConstant.NEWS_STATUS_THREE);
            siteMailService.updateNewsStatus(messageLog);
        }
        return response;
    }

    @ApiOperation(value="未读消息条数(总数)",notes="未读消息条数(总数)",response = Response.class)
    @RequestMapping(value = "sel_unreadNewsCount", method = RequestMethod.GET)
    public Response sel_unreadNewsCount()  {
        Response response = new Response();
        Long memberId = ShiroUtil.getCreateID();
        Map<String, Object> map = new HashMap<>();
        if(memberId!=null){
            map.put("recID",String.valueOf(memberId));
            map.put("status",MessageLogConstant.NEWS_STATUS_ONE);
            response.setData(siteMailService.selUnreadNewsCount(map));
        }else  {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value="我发送的留言列表",notes="我发送的留言列表",response = Response.class)
    @RequestMapping(value = "sel_mySendMsgList", method = RequestMethod.GET)
    public Response sel_mySendMsgList(@RequestParam(required = false,defaultValue = "1")String pageNo,
                                  @RequestParam(required = false,defaultValue = "10")String pageSize)  {
        Response response = new Response();

        Paging<Map<String,String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Long memberId = ShiroUtil.getCreateID();
        Map<String, Object> map = new HashMap<>();
        if(memberId!=null){
            map.put("id",memberId);
            List<Map<String,String>> list = siteMailService.findAllMySendMsgList(pager,map);
            pager.result(list);
            response.setData(pager);
        }else  {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value="查看我发送的留言详情",notes="查看我发送的留言详情",response = Response.class)
    @RequestMapping(value = "sel_mySendMsg", method = RequestMethod.GET)
    public Response sel_mySendMsg(@RequestParam String id)  {
        Response response = new Response();
        Map<String,String> map = siteMailService.queryMySendMsgById(id);
        response.setData(map);
        return response;
    }

    @ApiOperation(value="删除我发送的留言",notes="删除我发送的留言",response = Response.class)
    @RequestMapping(value = "del_sendMessage", method = RequestMethod.POST)
    public Response del_sendMessage(@ApiParam(value = "留言ids,逗号隔开") @RequestParam String ids)  {
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            Message message = new Message();
            message.setId(id);
            message.setSendDelete(MessageConstant.MESSAGE_SEND_DELETE_ONE);
            siteMailService.updateMessage(message);
        }
        return response;
    }

    @ApiOperation(value="我收到的留言列表",notes="我收到的留言列表",response = Response.class)
    @RequestMapping(value = "sel_myReceiveMsgList", method = RequestMethod.GET)
    public Response sel_myReceiveMsgList(@RequestParam(required = false,defaultValue = "1")String pageNo,
                                  @RequestParam(required = false,defaultValue = "10")String pageSize)  {
        Response response = new Response();

        Paging<Map<String,String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Long memberId = ShiroUtil.getCreateID();
        Map<String, Object> map = new HashMap<>();
        if(memberId!=null){
            map.put("id",memberId);
            List<Map<String,String>> list = siteMailService.findAllMyReceiveMsgList(pager,map);
            pager.result(list);
            response.setData(pager);
        }else  {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value="删除我收到的留言",notes="删除我收到的留言",response = Response.class)
    @RequestMapping(value = "del_receiveMessage", method = RequestMethod.POST)
    public Response del_receiveMessage(@ApiParam(value = "留言ids,逗号隔开") @RequestParam String ids)  {
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            Message message = new Message();
            message.setId(id);
            message.setReceiveDelete(MessageConstant.MESSAGE_RECEIVE_DELETE_ONE);
            siteMailService.updateMessage(message);
        }
        return response;
    }

    @ApiOperation(value="查看我收到的留言详情",notes="查看我收到的留言详情",response = Response.class)
    @RequestMapping(value = "sel_myReceiveMsg", method = RequestMethod.GET)
    public Response sel_myReceiveMsg(@RequestParam String id)  {
        Response response = new Response();
        Map<String,String> map = siteMailService.queryMyReceiveMsgById(id);
        response.setData(map);
        return response;
    }

    @ApiOperation(value="公告列表",notes="公告列表",response = Response.class)
    @RequestMapping(value = "sel_noticeList", method = RequestMethod.GET)
    public Response sel_noticeList(@RequestParam(required = false,defaultValue = "1")String pageNo,
                                   @RequestParam(required = false,defaultValue = "10")String pageSize)  {
        Response response = new Response();

        Paging<Map<String,String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map<String,String>> list = siteMailService.findAllNoticeList(pager);
        pager.result(list);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value="公告详情",notes="公告详情",response = Response.class)
    @RequestMapping(value = "sel_notice", method = RequestMethod.GET)
    public Response sel_notice(@RequestParam(required = false)String id)  {
        Response response = new Response();
        Map<String,String> map = siteMailService.queryNoticeById(id);
        response.setData(map);
        return response;
    }
}
