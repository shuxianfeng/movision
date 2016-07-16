package com.zhuhuibao.business.oms.platform;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.sitemail.entity.MessageLog;
import com.zhuhuibao.mybatis.sitemail.entity.MessageText;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by cxx on 2016/7/8 0008.
 */
@RestController
@RequestMapping("/rest/member/oms/mail")
public class MailOmsController {

    private final static Logger log = LoggerFactory.getLogger(MailOmsController.class);

    private static int num = 0;

    @Autowired
    SiteMailService siteMailService;

    @ApiOperation(value="条件查询用户",notes="条件查询用户",response = Response.class)
    @RequestMapping(value = "sel_member", method = RequestMethod.GET)
    public Response sel_member(@RequestParam(required = false)String accounts,
                               @ApiParam(value = "1:企业；2：个人")
                               @RequestParam(required = false)String memberType,
                               @RequestParam(required = false)String identify,
                               @ApiParam(value = "0：个人普通，30：个人黄金，60：个人铂金；100：企业普通，130：企业黄金，160：企业铂金；")
                                   @RequestParam(required = false)String level)  {
        Response response = new Response();
        Map<String, Object> map = new HashMap<>();
        map.put("accounts",accounts);
        map.put("memberType",memberType);
        map.put("identify",identify);
        map.put("level",level);
        List<Map<String,String>> list = siteMailService.findMember(map);
        response.setData(list);
        return response;
    }

    @ApiOperation(value="发送系统消息",notes="发送系统消息",response = Response.class)
    @RequestMapping(value = "send_news", method = RequestMethod.POST)
    public Response send_news(@RequestParam(required = false)String ids,
                               @RequestParam(required = false)String type,
                               @RequestParam(required = false)String title,
                               @RequestParam(required = false)String content)  {
        Response response = new Response();
        Long createid = ShiroUtil.getOmsCreateID();
        if(createid!=null){
            MessageText messageText = new MessageText();
            messageText.setMessageText(content);
            messageText.setType(Integer.parseInt(type));
            messageText.setSendID(createid);
            messageText.setTitle(title);
            Long messageID = siteMailService.addMsgText(messageText);

            MessageLog messageLog = new MessageLog();
            String[] idList = ids.split(",");
            for (String id : idList) {
                messageLog.setMessageID(messageID);
                messageLog.setRecID(Long.parseLong(id));
                messageLog.setStatus(1);
                siteMailService.addMsgLog(messageLog);
                num = num + 1;
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        return response;
    }


    @ApiOperation(value="统计发送进度",notes="统计发送进度",response = Response.class)
    @RequestMapping(value = "send_news_speed", method = RequestMethod.GET)
    public Response send_news_speed(@RequestParam(required = false)String ids)  {
        Response response = new Response();
        String[] idList = ids.split(",");
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        String result = numberFormat.format((float) num / (float) idList.length * 100);
        response.setData(result);
        return response;
    }
}
