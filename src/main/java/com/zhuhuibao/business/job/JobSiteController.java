package com.zhuhuibao.business.job;

import com.zhuhuibao.common.AskPriceResultBean;
import com.zhuhuibao.common.Constant;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.mybatis.memCenter.service.JobRelResumeService;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.mybatis.oms.entity.ChannelNews;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.mybatis.sitemail.entity.MessageText;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.file.ExporDoc;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/21 0021.
 */
@Controller
public class JobSiteController {
    private final static Logger log = LoggerFactory.getLogger(JobSiteController.class);

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

    @RequestMapping(value="/rest/job/applyPosition", method = RequestMethod.POST)
    public void applyPosition(HttpServletRequest req, HttpServletResponse response,String jobID,
                              String recID,String messageText) throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("applay position recID = "+recID+" messageText ="+messageText);
        Long createid = ShiroUtil.getCreateID();
        JsonResult jsonResult = new JsonResult();
        if(createid != null)
        {
            Resume rme = resume.queryResumeByCreateId(createid);
            if(rme != null && rme.getId() != null) {
                Long resumeID = Long.valueOf(rme.getId());
                MessageText msgText = new MessageText();
                msgText.setSendID(createid);
                msgText.setRecID(Long.valueOf(recID));
                msgText.setMessageText(messageText);
                msgText.setTypeID(resumeID);
                msgText.setType(Constant.sitemail_type_resume_one);
                jsonResult = smService.addSiteMail(msgText);
                jrrService.insert(Long.valueOf(jobID), resumeID);
            }
        }
        else
        {
            jsonResult.setCode(400);
            jsonResult.setMessage("you are not login!!!!");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 定义简历模板导出简历
     * @param req
     * @param response
     * @param resumeID 简历ID
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value="/rest/job/exportResume", method = RequestMethod.GET)
    public void exportResume(HttpServletRequest req, HttpServletResponse response,Long resumeID) throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("export resume id == "+resumeID);
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control",
                "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setContentType("application/msword");
        try {
            String path = req.getSession().getServletContext().getRealPath("/");
            log.info("base path = "+path);
            Map<String, String> resumeMap = resume.exportResume(String.valueOf(resumeID));
            if (!resumeMap.isEmpty()) {
                response.setHeader("Content-disposition", "attachment; filename=\""
                        + URLEncoder.encode(resumeMap.get("title"), "UTF-8") + ".doc\""); //
                HWPFDocument document = ExporDoc.replaceDoc(path + "resumeTemplate.doc", resumeMap);
                ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                document.write(ostream);
                ServletOutputStream stream = response.getOutputStream();
                stream.write(ostream.toByteArray());
                stream.flush();
                stream.close();
                stream.close();
            }
        }catch(IOException e){
                e.printStackTrace();
        }
    }

    @RequestMapping(value="/rest/job/queryCompanyInfo", method = RequestMethod.GET)
    public void queryCompanyInfo(HttpServletRequest req, HttpServletResponse response, Long id) throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("query company info id "+id);
        JsonResult jsonResult = job.queryCompanyInfo(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    @RequestMapping(value="/rest/job/queryAdvertisingPosition", method = RequestMethod.GET)
    public void queryAdvertisingPosition(HttpServletRequest req, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("query advertising postion");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("count",7);
        JsonResult jsonResult = job.queryAdvertisingPosition(map);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    @RequestMapping(value="/rest/job/queryPositionInfoByID", method = RequestMethod.GET)
    public void queryPositionInfoByID(HttpServletRequest req, HttpServletResponse response,Long id) throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("query position info by id");
        JsonResult jsonResult = job.queryPositionInfoByID(id);
        job.updateViews(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    @RequestMapping(value="/rest/job/queryOtherPosition", method = RequestMethod.GET)
    public void queryOtherPosition(HttpServletRequest req, HttpServletResponse response,
                                   String jobID,String createID,
                                   String pageNo,String pageSize) throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("query position info by id");
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Job> pager = new Paging<Job>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("createID",createID);
        map.put("jobID",jobID);
        JsonResult jsonResult = new JsonResult();
        List<Job> jobList = job.findAllOtherPosition(pager,map);
        pager.result(jobList);
        jsonResult.setData(pager);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 查询某个企业发布的职位
     * @param req
     * @param response
     * @param enterpriseID 创建者ID
     * @param city  城市代码
     * @param name
     * @param pageNo
     * @param pageSize
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value="/rest/job/queryPublishPositionByID", method = RequestMethod.GET)
    public void queryPublishPositionByID(HttpServletRequest req, HttpServletResponse response,
                                   String enterpriseID,String city,String name,
                                   String pageNo,String pageSize) throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("query position info by id");
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Job> pager = new Paging<Job>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("createID",enterpriseID);
        map.put("city",city);
        map.put("name",name);
        JsonResult jsonResult = new JsonResult();
        List<Job> jobList = job.findAllOtherPosition(pager,map);
        pager.result(jobList);
        jsonResult.setData(pager);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    @RequestMapping(value="/rest/job/queryAllPosition", method = RequestMethod.GET)
    public void queryAllPosition(HttpServletRequest req, HttpServletResponse response,
                                 String name,String enterpriseName,String province,String city,String area,String employeeNumber,
                                 String enterpriseType,String days,String salary,
                                 String pageNo,String pageSize) throws JsonGenerationException, JsonMappingException, IOException
    {
        JsonResult jsonResult = new JsonResult();
        log.info("query position info by id");
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Map<String,Object> map = new HashMap<String,Object>();
        if(name != null && !"".equals(name))
        {
            map.put("name",name.replace("_","\\_"));
        }
        map.put("enterpriseName",enterpriseName);
        map.put("city",city);
        map.put("employeeNumber",employeeNumber);
        map.put("enterpriseType",enterpriseType);
        //发布时间一周内，一天内
        if(days != null && !"".equals(days)) {
            Date date = DateUtils.date2Sub(new Date(), 5, -Integer.parseInt(days));
            String publishTime = DateUtils.date2Str(date, "yyyy-MM-dd");
            map.put("publishTime", publishTime);
        }
        map.put("salary",salary);
        Paging<Job> pager = new Paging<Job>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Job> jobList = job.findAllOtherPosition(pager,map);
        pager.result(jobList);
        jsonResult.setData(pager);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    @RequestMapping(value="/rest/job/findAllResume", method = RequestMethod.GET)
    public void findAllResume(HttpServletRequest req, HttpServletResponse response,
                              String title,String jobCity,String expYear,String education,
                              String pageNo,String pageSize) throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("find all resume!!");
        JsonResult jsonResult = new JsonResult();
        Long createId = ShiroUtil.getCreateID();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Resume> pager = new Paging<Resume>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<String, Object>();
        if(title != null && !"".equals(title))
        {
            map.put("title",title.replace("_","\\_"));
        }
        map.put("jobCity",jobCity);
        map.put("expYear",expYear);
        map.put("education",education);
        map.put("isPublic","1");
        jsonResult = resume.findAllResume(pager, map);
        jsonResult.setData(pager);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 查询推荐感兴趣的职位
     */
    @RequestMapping(value = "/rest/job/queryRecommendPosition", method = RequestMethod.GET)
    public void queryRecommendPosition(HttpServletRequest req, HttpServletResponse response,String postID) throws IOException {
        //查询不同公司发布的相同职位
        JsonResult jsonResult = job.searchSamePosition(postID);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 热门招聘
     */
    @RequestMapping(value = "/rest/job/queryHotPosition", method = RequestMethod.GET)
    public void queryHotPosition(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult jsonResult = job.queryHotPosition(9);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 最新求职
     */
    @RequestMapping(value = "/rest/job/queryLatestResume", method = RequestMethod.GET)
    public void queryLatestResume(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult jsonResult = resume.queryLatestResume(9);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 最新招聘（按分类一起查询）
     */
    @RequestMapping(value = "/rest/job/queryLatestJob", method = RequestMethod.GET)
    public void queryLatestJob(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult jsonResult = job.queryLatestJob(5);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 名企招聘
     */
    @RequestMapping(value = "/rest/job/greatCompanyPosition", method = RequestMethod.GET)
    public void greatCompanyPosition(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult jsonResult = job.greatCompanyPosition();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 招聘会信息列表
     */
    @RequestMapping(value = "/rest/job/queryJobMeetingInfo", method = RequestMethod.GET)
    public void queryJobMeetingInfo(HttpServletRequest req, HttpServletResponse response,String pageSize,String pageNo) throws IOException {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        JsonResult jsonResult = new JsonResult();
        Paging<ChannelNews> pager = new Paging<ChannelNews>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        List<ChannelNews> list = newsService.findAllNewsByChannelInfo(pager);
        pager.result(list);
        jsonResult.setData(pager);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 根据ID查询招聘会信息
     */
    @RequestMapping(value = "/rest/job/queryJobMeetingInfoById", method = RequestMethod.GET)
    public void queryJobMeetingInfoById(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        JsonResult jsonResult = newsService.queryJobMeetingInfoById(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 相似企业
     */
    @RequestMapping(value = "/rest/job/querySimilarCompany", method = RequestMethod.GET)
    public void querySimilarCompany(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        JsonResult jsonResult = job.querySimilarCompany(id,4);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }
}
