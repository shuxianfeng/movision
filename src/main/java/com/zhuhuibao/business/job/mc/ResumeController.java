package com.zhuhuibao.business.job.mc;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ApiConstants;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by cxx on 2016/4/19 0019.
 */
@RestController
@RequestMapping("/rest/job")
@Api(value="Resume", description="会员中心-简历管理")
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
    @ApiOperation(value = "发布简历", notes = "发布简历", response = Response.class)
    @RequestMapping(value = {"setUpResume","mc/resume/add_resume"}, method = RequestMethod.POST)
    public Response setUpResume(Resume resume) throws IOException {
        Long createid = ShiroUtil.getCreateID();
        resume.setCreateid(createid.toString());
        Response response = resumeService.setUpResume(resume,createid.toString());
        return response;
    }

    /**
     * 查询我创建的简历
     */
    @ApiOperation(value = "查询我创建的简历", notes = "查询我创建的简历", response = Response.class)
    @RequestMapping(value = {"searchMyResume","mc/resume/sel_my_resume"}, method = RequestMethod.GET)
    public Response searchMyResume() throws IOException {
        Long createid = ShiroUtil.getCreateID();
        Response response = resumeService.searchMyResume(createid.toString());
        return response;
    }

    /**
     * 更新简历,刷新简历
     */
    @ApiOperation(value = "更新简历", notes = "更新简历", response = Response.class)
    @RequestMapping(value = {"updateResume","mc/resume/upd_resume"}, method = RequestMethod.POST)
    public Response updateResume(Resume resume) throws IOException {
        return resumeService.updateResume(resume);
    }

    /**
     * 预览简历
     */
    @ApiOperation(value = "预览简历", notes = "预览简历", response = Response.class)
    @RequestMapping(value = {"previewResume","mc/resume/preview_resume"}, method = RequestMethod.GET)
    public Response previewResume(String id) throws Exception {
        return resumeService.previewResume(id);
    }

    /**
     * 上传简历附件
     */
    @ApiOperation(value = "上传简历附件", notes = "上传简历附件", response = Response.class)
    @RequestMapping(value = {"uploadResume","mc/resume/upload_resume"}, method = RequestMethod.POST)
    public Response uploadResume(HttpServletRequest req) throws IOException {
        Response result = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session){
            String url = uploadService.upload(req,"job");
            Map map = new HashMap();
            map.put(Constants.name,url);
            result.setData(map);
            result.setCode(200);
        }else{
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    /**
     * 查询我创建的简历的全部信息
     */
    @ApiOperation(value = "查询我创建的简历的全部信息", notes = "查询我创建的简历的全部信息", response = Response.class)
    @RequestMapping(value = {"searchMyResumeAllInfo","mc/resume/sel_my_resume_info"}, method = RequestMethod.GET)
    public Response searchMyResumeAllInfo() throws IOException {
        Long createid = ShiroUtil.getCreateID();
        Response response = resumeService.searchMyResumeAllInfo(createid.toString());
        return response;
    }

    /**
     * 我收到的简历
     */
    @ApiOperation(value = "我收到的简历", notes = "我收到的简历", response = Response.class)
    @RequestMapping(value = {"receiveResume","mc/resume/sel_receive_resume"}, method = RequestMethod.GET)
    public Response receiveResume(String pageNo, String pageSize) throws IOException {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Long createid = ShiroUtil.getCreateID();
        Paging<Resume> pager = new Paging<Resume>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        Response response = resumeService.receiveResume(pager,createid.toString());
        return response;
    }

    /**
     * 下载简历附件
     */
    @ApiOperation(value = "下载简历附件", notes = "下载简历附件")
    @RequestMapping(value = {"downLoadResume","mc/resume/download_resume"}, method = RequestMethod.GET)
    public void downLoadResume(HttpServletResponse response, String id) throws IOException {
        Response jsonResult = new Response();
        try {
            String fileurl = resumeService.downloadBill(id);
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control",
                    "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Content-disposition", "attachment;filename=" + fileurl);
            response.setContentType("application/octet-stream");
            fileurl = ApiConstants.getUploadDoc() + Constants.upload_job_document_url + "/" + fileurl;
            jsonResult = FileUtil.downloadFile(response, fileurl);
        }
        catch(Exception e)
        {
            log.error("download resume error! ",e);
        }
    }
}
