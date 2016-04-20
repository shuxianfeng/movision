package com.zhuhuibao.business.memCenter.JobManage;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
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
 * Created by cxx on 2016/4/19 0019.
 */
@RestController
public class ResumeController {
    private static final Logger log = LoggerFactory.getLogger(ResumeController.class);

    @Autowired
    private ResumeService resumeService;

    /**
     * 发布简历
     */
    @RequestMapping(value = "rest/job/setUpResume", method = RequestMethod.POST)
    public void setUpResume(HttpServletRequest req, HttpServletResponse response, Resume resume) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        JsonResult jsonResult = new JsonResult();
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                resume.setCreateid(principal.getId().toString());
                jsonResult = resumeService.setUpResume(resume);
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 查询我创建的简历
     */
    @RequestMapping(value = "rest/job/searchMyResume", method = RequestMethod.GET)
    public void searchMyResume(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        JsonResult jsonResult = new JsonResult();
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                jsonResult = resumeService.searchMyResume(principal.getId().toString());
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 更新简历,刷新简历
     */
    @RequestMapping(value = "rest/job/updateResume", method = RequestMethod.GET)
    public void updateResume(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        JsonResult jsonResult = resumeService.updateResume(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 预览简历
     */
    @RequestMapping(value = "rest/job/previewResume", method = RequestMethod.GET)
    public void previewResume(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        JsonResult jsonResult = resumeService.previewResume(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }
}
