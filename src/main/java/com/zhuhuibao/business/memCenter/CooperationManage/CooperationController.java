package com.zhuhuibao.business.memCenter.CooperationManage;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.Cooperation;
import com.zhuhuibao.mybatis.memCenter.service.CooperationService;
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
 * Created by cxx on 2016/5/4 0004.
 */
@RestController
public class CooperationController {
    private static final Logger log = LoggerFactory
            .getLogger(CooperationController.class);

    @Autowired
    private CooperationService cooperationService;
    /**
     * 发布任务
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/cooperation/publishCooperation", method = RequestMethod.POST)
    public void publishCooperation(HttpServletRequest req, HttpServletResponse response, Cooperation cooperation) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        JsonResult jsonResult = new JsonResult();
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if(null != principal){
                cooperation.setCreateId(principal.getId().toString());
                jsonResult = cooperationService.publishCooperation(cooperation);
            }else{
                jsonResult.setCode(401);
                jsonResult.setMessage("请先登录");
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 合作类型
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/cooperation/cooperationType", method = RequestMethod.GET)
    public void cooperationType(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult jsonResult = cooperationService.cooperationType();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }
}
