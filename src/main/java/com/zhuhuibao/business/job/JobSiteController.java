package com.zhuhuibao.business.job;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.AskPriceResultBean;
import com.zhuhuibao.common.Constant;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.constant.JobConstant;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Administrator on 2016/4/21 0021.
 */
@RestController
@RequestMapping("/rest/jobsite/")
@Api(value="jobsite", description="人才网")
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

    @RequestMapping(value="applyPosition", method = RequestMethod.POST)
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
                msgText.setType(JobConstant.SITEMAIL_TYPE_JOB_ELEVEN);
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
    @RequestMapping(value="exportResume", method = RequestMethod.GET)
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

    @RequestMapping(value="queryCompanyInfo", method = RequestMethod.GET)
    @ApiOperation(value="公司详情",notes = "公司详情",response = JsonResult.class)
    public void queryCompanyInfo(HttpServletRequest req, HttpServletResponse response,
                                 @ApiParam(value = "创建者ID(会员ID)") @RequestParam(required = true) Long id) throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("query company info id "+id);
        JsonResult jsonResult = job.queryCompanyInfo(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    @RequestMapping(value="queryAdvertisingPosition", method = RequestMethod.GET)
    public void queryAdvertisingPosition(HttpServletRequest req, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("query advertising postion");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("count",7);
        JsonResult jsonResult = job.queryAdvertisingPosition(map);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    @RequestMapping(value="queryPositionInfoByID", method = RequestMethod.GET)
    public void queryPositionInfoByID(HttpServletRequest req, HttpServletResponse response,Long id) throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("query position info by id");
        JsonResult jsonResult = job.queryPositionInfoByID(id);
        job.updateViews(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    @RequestMapping(value="queryOtherPosition", method = RequestMethod.GET)
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
    @RequestMapping(value="queryPublishPositionByID", method = RequestMethod.GET)
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

    @RequestMapping(value="queryAllPosition", method = RequestMethod.GET)
    @ApiOperation(value="职位搜索",notes = "职位频道页搜索",response = JsonResult.class)
    public void queryAllPosition(HttpServletRequest req, HttpServletResponse response,
                                 @ApiParam(value="公司名称或企业名称") @RequestParam(required = false) String name,
                                 @ApiParam(value="省代码") @RequestParam(required = false) String province,
                                 @ApiParam(value="市代码") @RequestParam(required = false)String city,
                                 @ApiParam(value="区代码") @RequestParam(required = false) String area,
                                 @ApiParam(value="企业规模") @RequestParam(required = false) String employeeNumber,
                                 @ApiParam(value="企业性质") @RequestParam(required = false)String enterpriseType,
                                 @ApiParam(value="发布时间") @RequestParam(required = false)String days,
                                 @ApiParam(value="薪资") @RequestParam(required = false)String salary,
                                 @ApiParam(value="职位类别")@RequestParam(required = false) String positionType,
                                 @ApiParam(value="页码")@RequestParam(required = false) String pageNo,
                                 @ApiParam(value="每页显示的数目")@RequestParam(required = false) String pageSize) throws JsonGenerationException, JsonMappingException, IOException
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
        map.put("positionType",positionType);
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

    @RequestMapping(value="findAllResume", method = RequestMethod.GET)
    @ApiOperation(value="人才库搜索",notes = "人才库搜索",response = JsonResult.class)
    public void findAllResume(HttpServletRequest req, HttpServletResponse response,
                              @ApiParam(value="简历名称") @RequestParam(required = false) String title,
                              @ApiParam(value="期望工作城市") @RequestParam(required = false) String jobCity,
                              @ApiParam(value="工作经验") @RequestParam(required = false)String expYear,
                              @ApiParam(value="学历") @RequestParam(required = false)String education,
                              @ApiParam(value="职位类别") @RequestParam(required = false)String positionType,
                              @ApiParam(value="页码") @RequestParam(required = false)String pageNo,
                              @ApiParam(value="每页显示的数目") @RequestParam(required = false)String pageSize) throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("find all resume!!");
        JsonResult jsonResult = new JsonResult();
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
        if(positionType != null && positionType.length() > 0)
        {
            String[] positionTypes = positionType.split(",");
            List<String> positionList = Arrays.asList(positionTypes);
            map.put("positionList",positionList);
        }
        jsonResult = resume.findAllResume(pager, map);
        jsonResult.setData(pager);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 查询推荐感兴趣的职位
     */
    @RequestMapping(value = "queryRecommendPosition", method = RequestMethod.GET)
    public void queryRecommendPosition(HttpServletRequest req, HttpServletResponse response,String postID) throws IOException {
        //查询不同公司发布的相同职位
        JsonResult jsonResult = job.searchSamePosition(postID);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 热门招聘
     */
    @RequestMapping(value = "queryHotPosition", method = RequestMethod.GET)
    public void queryHotPosition(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult jsonResult = job.queryHotPosition(9);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 最新求职
     */
    @RequestMapping(value = "queryLatestResume", method = RequestMethod.GET)
    public void queryLatestResume(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult jsonResult = resume.queryLatestResume(9);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 最新招聘（按分类一起查询）
     */
    @RequestMapping(value = "queryLatestJob", method = RequestMethod.GET)
    public void queryLatestJob(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult jsonResult = job.queryLatestJob(5);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 名企招聘
     */
    @RequestMapping(value = "greatCompanyPosition", method = RequestMethod.GET)
    public void greatCompanyPosition(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult jsonResult = job.greatCompanyPosition();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    @RequestMapping(value = "querySimilarCompany", method = RequestMethod.GET)
    @ApiOperation(value = "相似企业",notes = "相似企业",response = JsonResult.class)
    public JsonResult querySimilarCompany(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        JsonResult jsonResult = job.querySimilarCompany(id,4);
        return jsonResult;
    }

    @RequestMapping(value = "queryEnterpriseHotPosition", method = RequestMethod.GET)
    @ApiOperation(value="热门职位", notes="查询名企发布的热门职位", response=JsonResult.class)
    public JsonResult queryEnterpriseHotPosition() throws IOException {
        JsonResult jsonResult = new JsonResult();
        Map<String,Object> map = new HashMap<String,Object>();
        //“1”推荐企业.
        map.put("recommend", JobConstant.JOB_RECOMMEND_TRUE);
        map.put("count",JobConstant.JOB_HOTPOSITION_COUNT_EIGHT);
        List<Job> jobList = job.queryEnterpriseHotPosition(map);
        jsonResult.setData(jobList);
        return jsonResult;
    }

    @RequestMapping(value="isExistResume",method=RequestMethod.GET)
    @ApiOperation(value = "判断是否已经创建简历",notes = "判断是否已经创建简历",response=JsonResult.class)
    public JsonResult isExistResume(@ApiParam(value = "创建者ID或者会员ID") @RequestParam Long createID) throws Exception
    {
        JsonResult jsonResult = new JsonResult();
        Boolean isExist = resume.isExistResume(createID);
        jsonResult.setData(isExist);
        return jsonResult;
    }

    @RequestMapping(value="queryUnreadMsgCount",method = RequestMethod.GET)
    @ApiOperation(value="未读消息数目",notes = "人才网未读消息数目",response = JsonResult.class)
    public JsonResult queryUnreadMsgCount(@ApiParam(value = "登陆会员ID") @RequestParam Long receiveID)
    {
        JsonResult jsonResult = new JsonResult();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("recID",receiveID);
        map.put("type",JobConstant.SITEMAIL_TYPE_JOB_ELEVEN);
        map.put("status",Constant.MAILSITE_STATUS_UNREAD);
        jsonResult.setData(smService.queryUnreadMsgCount(map));
        return jsonResult;
    }
}
