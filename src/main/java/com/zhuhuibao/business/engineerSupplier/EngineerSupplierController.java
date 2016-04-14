package com.zhuhuibao.business.engineerSupplier;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.Message;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.JsonUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by cxx on 2016/4/11 0011.
 */
@RestController
public class EngineerSupplierController {
    private static final Logger log = LoggerFactory
            .getLogger(EngineerSupplierController.class);

    @Autowired
    private MemberService memberService;
    /**
     *最新工程商(个数后台控制)
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/engineerSupplier/newEngineer", method = RequestMethod.GET)
    public void newEngineer(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String type = "4";
        JsonResult jsonResult = memberService.findNewEngineerOrSupplier(type);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     *最新厂商(个数后台控制)
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/engineerSupplier/newManufacturer", method = RequestMethod.GET)
    public void newManufacturer(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String type = "1";
        JsonResult jsonResult = memberService.findNewEngineerOrSupplier(type);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     *最新代理商(个数后台控制)
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/engineerSupplier/newAgent", method = RequestMethod.GET)
    public void newAgent(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String type = "2";
        JsonResult jsonResult = memberService.findNewEngineerOrSupplier(type);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     *最新渠道商(个数后台控制)
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/engineerSupplier/newChannel", method = RequestMethod.GET)
    public void newChannel(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String type = "3";
        JsonResult jsonResult = memberService.findNewEngineerOrSupplier(type);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     *工程商，供应商简版介绍
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/engineerSupplier/introduce", method = RequestMethod.GET)
    public void introduce(HttpServletRequest req, HttpServletResponse response,String id,String type) throws IOException {
        JsonResult jsonResult = memberService.introduce(id,type);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     *名企展示
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/engineerSupplier/greatCompany", method = RequestMethod.GET)
    public void greatCompany(HttpServletRequest req, HttpServletResponse response,String type) throws IOException {
        JsonResult jsonResult = memberService.greatCompany(type);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     *留言
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/engineerSupplier/message", method = RequestMethod.POST)
    public void message(HttpServletRequest req, HttpServletResponse response, Message message) throws IOException {
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
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }
}
