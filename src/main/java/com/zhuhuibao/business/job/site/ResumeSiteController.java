package com.zhuhuibao.business.job.site;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.JobConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.PageNotFoundException;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.mybatis.memCenter.service.JobRelResumeService;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.mybatis.payment.service.PaymentGoodsService;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.file.ExporDoc;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;


@RestController
@RequestMapping("/rest/job/site/resume")
public class ResumeSiteController {
    private final static Logger log = LoggerFactory.getLogger(ResumeSiteController.class);

    @Autowired
    JobPositionService job;

    @Autowired
    ResumeService resume;

    @Autowired
    SiteMailService smService;

    @Autowired
    JobRelResumeService jrrService;

    @Autowired
    ChannelNewsService newsService;

    @Autowired
    PaymentGoodsService goodsService;

    @RequestMapping(value = "export_resume", method = RequestMethod.GET)
    @ApiOperation(value = "定义简历模板导出简历", notes = "定义简历模板导出简历")
    public void exportResume(HttpServletRequest req, HttpServletResponse response,
                             @ApiParam(value = "简历ID") @RequestParam Long resumeID) {
        Long memberId = ShiroUtil.getCreateID();
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("id", resumeID);
        queryMap.put("createid", memberId);
        List<Map<String, String>> resultList = jrrService.queryReceiveResume(queryMap);
        if (resultList.size()!=0) {
            log.info("export resume id == " + resumeID);
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control",
                    "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setContentType("application/msword");
            try {
                String path = req.getSession().getServletContext().getRealPath("/");
                log.info("base path = " + path);

                Map<String, String> resumeMap = resume.exportResume(String.valueOf(resumeID));
                if (!resumeMap.isEmpty()) {
                    String fileName = !StringUtils.isEmpty(resumeMap.get("title")) ? resumeMap.get("title") : "简历";
                    response.setHeader("Content-disposition", "attachment; filename=\""
                            + URLEncoder.encode(fileName, "UTF-8") + ".doc\"");
                    HWPFDocument document = ExporDoc.replaceDoc(path + "resumeTemplate.doc", resumeMap);
                    ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                    if (document != null) {
                        document.write(ostream);
                    }
                    ServletOutputStream stream = response.getOutputStream();
                    stream.write(ostream.toByteArray());
                    stream.flush();
                    stream.close();
                    stream.close();

                    Resume resumeBean = new Resume();
                    resumeBean.setDownload("1");
                    resumeBean.setId(String.valueOf(resumeID));
                    resume.updateResume(resumeBean);

                }
            } catch (Exception e) {
                log.error("执行异常>>>", e);
            }
        } else {
            throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
        }
    }

    @RequestMapping(value = "sel_all_resume", method = RequestMethod.GET)
    @ApiOperation(value = "人才库搜索", notes = "人才库搜索", response = Response.class)
    public Response findAllResume(@ApiParam(value = "关键字") @RequestParam(required = false) String title,
                                  @ApiParam(value = "期望工作城市") @RequestParam(required = false) String jobCity,
                                  @ApiParam(value = "工作年限前") @RequestParam(required = false) String expYearBefore,
                                  @ApiParam(value = "工作年限后") @RequestParam(required = false) String expYearBehind,
                                  @ApiParam(value = "学历") @RequestParam(required = false) String education,
                                  @ApiParam(value = "职位类别") @RequestParam(required = false) String positionType,
                                  @ApiParam(value = "是否公开") @RequestParam(required = false) String isPublic,
                                  @ApiParam(value = "页码") @RequestParam(required = false,defaultValue = "1") String pageNo,
                                  @ApiParam(value = "每页显示的数目") @RequestParam(required = false,defaultValue = "10") String pageSize) throws IOException {
        log.info("find all resume!!");
        Response response = new Response();

        Paging<Map<String, Object>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        if (title != null && !"".equals(title)) {
            map.put("title", title.replace("_", "\\_"));
        }
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (session != null) {
            //简历屏蔽
            map.put("company_id", ShiroUtil.getCreateID());
        }
        map.put("jobCity", jobCity);
        /*if(expYearBefore != null && !expYearBefore.equals("") && expYearBehind == null && expYearBehind.equals("")) {
            map.put("expYearBeforeFlag", "true");
            map.put("expYearBefore", expYearBefore);
        }*/
        map.put("expYearBefore", expYearBefore);
        map.put("expYearBehind", expYearBehind);
        map.put("education", education);
        if (isPublic == null) {
            map.put("isPublic", "1");	//默认公开
        } else {
            if (!"2".equals(isPublic)) {
                map.put("isPublic", isPublic);
            }
        }
        map.put("status", JobConstant.JOB_MEMBER_STATUS_LOGOUT);
        if (positionType != null && positionType.length() > 0) {
            String[] positionTypes = positionType.split(",");
            List<String> positionList = Arrays.asList(positionTypes);
            map.put("positionList", positionList);
        }
        pager = resume.findAllResume(pager, map);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value = "sel_latest_resume", method = RequestMethod.GET)
    @ApiOperation(value = "人才网首页最新求职 默认9个", notes = "人才网首页最新求职    默认9个", response = Response.class)
    public Response queryLatestResume(@ApiParam("限制条数") @RequestParam(required = false) String count)
            throws Exception {
        Map<String, Object> condition = new HashMap<>();
        if (StringUtils.isEmpty(count)) {
            condition.put("count", JobConstant.JOB_RESUME_LATEST_COUNT_NINE);
        } else {
            condition.put("count", Integer.valueOf(count));
        }
        condition.put("public", JobConstant.JOB_RESUME_STATUS_PUBLIC);
        condition.put("status", JobConstant.JOB_MEMBER_STATUS_LOGOUT);
        Response response = new Response();
        List resumeList = resume.queryLatestResume(condition);
        response.setData(resumeList);
        return response;
    }

    @RequestMapping(value = "isExist_resume", method = RequestMethod.GET)
    @ApiOperation(value = "判断是否已经创建简历", notes = "判断是否已经创建简历", response = Response.class)
    public Response isExistResume() throws Exception {
        Response response = new Response();
        Long createID = ShiroUtil.getCreateID();
        if (createID != null) {
            Boolean isExist = resume.isExistResume(createID);
            response.setData(isExist);
        } else {
            response.setData(false);
        }
        return response;
    }

    @ApiOperation(value = "会员中心查看我收到的简历", notes = "会员中心查看我收到的简历", response = Response.class)
    @RequestMapping(value = "preview_resume", method = RequestMethod.GET)
    public Response previewResume(@ApiParam(value = "简历id") @RequestParam String id,
                                  @ApiParam(value = "该投递简历记录的id,频道页不传，会员中心查看简历记录时候传") @RequestParam(required = false) String recordId) throws Exception {
        Long memberId = ShiroUtil.getCreateID();
        Long companyId = ShiroUtil.getCompanyID();
        Response response = new Response();
        if (memberId != null) {
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("id", id);
            queryMap.put("recordId", recordId);
            Map<String, String> result = jrrService.queryMyReceiveResume(queryMap);
            if (result != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", recordId);
                map.put("status", JobConstant.RESUME_STATUS_TWO);
                jrrService.updateJobRelResume(map);
                Resume resumeBean = new Resume();
                resumeBean.setViews("1");
                resumeBean.setId(id);
                resume.updateResume(resumeBean);
                //发布招聘职位已付过钱，查看应聘的简历，也属于付费查看过的简历,需要增加到已付费信息表。这部分信息不需要付费
                goodsService.insertViewGoods(Long.parseLong(id), memberId, companyId, ZhbPaymentConstant.goodsType.CXXZJL.toString());
                Map<String, Object> map1 = new HashMap<>();
                map1.put("resumeID", id);
                map1.put("companyID", companyId);
                Map<String, Object> queryMap1 = new HashMap<>();
                queryMap1.put("id", id);
                Resume resume2 = resume.previewResume(queryMap1);
                map1.put("createId", resume2.getCreateid());
                resume.addLookRecord(map1);
                response.setData(resume2);
            } else {
                throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
            }
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @RequestMapping(value = "upd_coll_resume", method = RequestMethod.POST)
    @ApiOperation(value = "收藏简历", notes = "收藏简历", response = Response.class)
    public Response insertCollResume(@ApiParam(value = "简历id") @RequestParam String id) throws Exception {

        Response response = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if (memberId != null) {
            int collCount = resume.getMaxCollCount(memberId);
            if (collCount >= JobConstant.MAX_COLL_COUNT) {
                response.setCode(400);
                response.setMessage("您的简历收藏夹已满" + JobConstant.MAX_COLL_COUNT + "，请先清空收藏夹，然后再进行简历收藏！");
                return response;
            }
            int result = resume.insertCollRecord(id);
            if (result > 0) {
                response.setCode(200);
            } else {
                response.setCode(400);
            }

        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        return response;
    }

}
