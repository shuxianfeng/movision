package com.zhuhuibao.business.engineerSupplier;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.mybatis.memCenter.entity.Message;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/4/11 0011.
 */
@RestController
@RequestMapping(value = "/rest/engineerSupplier")
public class EngineerSupplierController {
    private static final Logger log = LoggerFactory.getLogger(EngineerSupplierController.class);

    @Autowired
    private MemberService memberService;
    /**
     *最新工程商(个数后台控制)
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "newEngineer", method = RequestMethod.GET)
    public Response newEngineer() throws Exception {
        String type = "4";
        Response response = new Response();
        List list = memberService.findNewEngineerOrSupplier(type);
        response.setData(list);
        return response;
    }


    /**
     *最新厂商(个数后台控制)
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "newManufacturer", method = RequestMethod.GET)
    public Response newManufacturer() throws Exception {
        String type = "1";
        Response response = new Response();
        List list = memberService.findNewEngineerOrSupplier(type);
        response.setData(list);
        return response;
    }

    /**
     *最新代理商(个数后台控制)
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "newAgent", method = RequestMethod.GET)
    public Response newAgent() throws Exception {
        String type = "2";
        Response response = new Response();
        List list = memberService.findNewEngineerOrSupplier(type);
        response.setData(list);
        return response;
    }

    /**
     *最新渠道商(个数后台控制)
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "newChannel", method = RequestMethod.GET)
    public Response newChannel() throws Exception {
        String type = "3";
        Response response = new Response();
        List list = memberService.findNewEngineerOrSupplier(type);
        response.setData(list);
        return response;
    }

    /**
     *工程商，供应商简版介绍
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "introduce", method = RequestMethod.GET)
    public Response introduce(String id, String type) throws Exception {
        Response response = new Response();
        Map map = memberService.introduce(id,type);
        response.setData(map);
        return response;
    }

    /**
     *名企展示
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "greatCompany", method = RequestMethod.GET)
    public Response greatCompany(String type) throws Exception {
        Response response = new Response();
        List list = memberService.greatCompany(type);
        response.setData(list);
        return response;
    }

    /**
     *最新认证工程商,供应商(个数后台控制)
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "newIdentifyEngineer", method = RequestMethod.GET)
    public Response newIdentifyEngineer(String type) throws Exception {
        Response response = new Response();
        List list = memberService.findnewIdentifyEngineer(type);
        response.setData(list);
        return response;
    }

    /**
     *留言
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "message", method = RequestMethod.POST)
    public Response message(Message message) throws Exception {
/*        String title = new String(message.getTitle().getBytes("8859_1"), "utf8" );
        String receiveName = new String(message.getReceiveName().getBytes("8859_1"), "utf8" );
        String content = new String(message.getContent().getBytes("8859_1"), "utf8" );*/
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session)
        {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                message.setCreateid(principal.getId().toString());
                memberService.saveMessage(message);
            }else{
                response.setCode(401);
                response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                response.setMsgCode(MsgCodeConstant.un_login);
            }
        }else{
            response.setCode(401);
            response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            response.setMsgCode(MsgCodeConstant.un_login);
        }
        return response;
    }
}
