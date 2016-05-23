package com.zhuhuibao.business.engineerSupplier;

import com.zhuhuibao.common.JsonResult;
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
    public JsonResult newEngineer() throws IOException {
        String type = "4";
        return memberService.findNewEngineerOrSupplier(type);
    }


    /**
     *最新厂商(个数后台控制)
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "newManufacturer", method = RequestMethod.GET)
    public JsonResult newManufacturer() throws IOException {
        String type = "1";
        return  memberService.findNewEngineerOrSupplier(type);
    }

    /**
     *最新代理商(个数后台控制)
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "newAgent", method = RequestMethod.GET)
    public JsonResult newAgent() throws IOException {
        String type = "2";
        return memberService.findNewEngineerOrSupplier(type);
    }

    /**
     *最新渠道商(个数后台控制)
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "newChannel", method = RequestMethod.GET)
    public JsonResult newChannel() throws IOException {
        String type = "3";
        return  memberService.findNewEngineerOrSupplier(type);
    }

    /**
     *工程商，供应商简版介绍
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "introduce", method = RequestMethod.GET)
    public JsonResult introduce(String id, String type) throws IOException {
        return memberService.introduce(id,type);
    }

    /**
     *名企展示
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "greatCompany", method = RequestMethod.GET)
    public JsonResult greatCompany(String type) throws IOException {
        return memberService.greatCompany(type);
    }

    /**
     *最新认证工程商,供应商(个数后台控制)
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "newIdentifyEngineer", method = RequestMethod.GET)
    public JsonResult newIdentifyEngineer(String type) throws IOException {
        return  memberService.findnewIdentifyEngineer(type);
    }

    /**
     *留言
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "message", method = RequestMethod.POST)
    public JsonResult message(Message message) throws IOException {
/*        String title = new String(message.getTitle().getBytes("8859_1"), "utf8" );
        String receiveName = new String(message.getReceiveName().getBytes("8859_1"), "utf8" );
        String content = new String(message.getContent().getBytes("8859_1"), "utf8" );*/
        JsonResult jsonResult = new JsonResult();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session)
        {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                message.setCreateid(principal.getId().toString());
                jsonResult = memberService.saveMessage(message);
            }else{
                jsonResult.setCode(401);
                jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                jsonResult.setMsgCode(MsgCodeConstant.un_login);
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            jsonResult.setMsgCode(MsgCodeConstant.un_login);
        }

        return jsonResult;
    }
}
