package com.zhuhuibao.business.memCenter.JobManage;

import com.zhuhuibao.common.ApiConstants;
import com.zhuhuibao.common.Constant;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.file.FileUtil;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
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
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cxx on 2016/4/19 0019.
 */
@RestController
public class ResumeController {
    private static final Logger log = LoggerFactory.getLogger(ResumeController.class);

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    ApiConstants ApiConstants;
    /**
     * 发布简历
     */
    @RequestMapping(value = "/rest/job/setUpResume", method = RequestMethod.POST)
    public void setUpResume(HttpServletRequest req, HttpServletResponse response, Resume resume) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        JsonResult jsonResult = new JsonResult();
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                resume.setCreateid(principal.getId().toString());
                jsonResult = resumeService.setUpResume(resume,principal.getId().toString());
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
    @RequestMapping(value = "/rest/job/searchMyResume", method = RequestMethod.GET)
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
    @RequestMapping(value = "/rest/job/updateResume", method = RequestMethod.POST)
    public void updateResume(HttpServletRequest req, HttpServletResponse response,Resume resume) throws IOException {
        JsonResult jsonResult = resumeService.updateResume(resume);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 预览简历
     */
    @RequestMapping(value = "/rest/job/previewResume", method = RequestMethod.GET)
    public void previewResume(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        JsonResult jsonResult = resumeService.previewResume(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 上传简历附件
     */
    @RequestMapping(value = "/rest/price/uploadResume", method = RequestMethod.POST)
    public void uploadResume(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult result = new JsonResult();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session){
            String url = uploadService.upload(req,"job");
            Map map = new HashMap();
            map.put(Constant.name,url);
            result.setData(map);
            result.setCode(200);
        }else{
            result.setCode(401);
            result.setMessage("请先登录");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 查询我创建的简历的全部信息
     */
    @RequestMapping(value = "/rest/job/searchMyResumeAllInfo", method = RequestMethod.GET)
    public void searchMyResumeAllInfo(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        JsonResult jsonResult = new JsonResult();
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                jsonResult = resumeService.searchMyResumeAllInfo(principal.getId().toString());
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 我收到的简历
     */
    @RequestMapping(value = "/rest/job/receiveResume", method = RequestMethod.GET)
    public void receiveResume(HttpServletRequest req, HttpServletResponse response,String pageNo,String pageSize) throws IOException {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        JsonResult jsonResult = new JsonResult();
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if(null != principal) {
                Paging<Resume> pager = new Paging<Resume>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
                jsonResult = resumeService.receiveResume(pager,principal.getId().toString());
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 下载简历附件
     */
    @RequestMapping(value = "/rest/job/downLoadResume", method = RequestMethod.GET)
    public void downLoadResume(HttpServletRequest req, HttpServletResponse response,String id,String url) throws IOException {
        JsonResult jsonResult = new JsonResult();
        try {
            String fileurl = resumeService.downloadBill(id);
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control",
                    "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Content-disposition", "attachment;filename=" + fileurl);
            response.setContentType("application/octet-stream");
            fileurl = ApiConstants.getUploadDoc() + Constant.upload_job_document_url + "/" + fileurl;
            jsonResult = FileUtil.downloadFile(response, fileurl);
        }
        catch(Exception e)
        {
            log.error("download resume error! ",e);
        }
    }
}
