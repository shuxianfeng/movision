package com.zhuhuibao.business.job;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.mybatis.sitemail.entity.MessageText;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.ExporDoc;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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

    @RequestMapping(value="/rest/job/applyPosition", method = RequestMethod.GET)
    public void applyPosition(HttpServletRequest req, HttpServletResponse response,MessageText msgText) throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("applay position "+msgText.getMessageText());
        JsonResult jsonResult = smService.addSiteMail(msgText);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    @RequestMapping(value="/rest/job/exportResume", method = RequestMethod.GET)
    public void exportResume(HttpServletRequest req, HttpServletResponse response,Long id) throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("export resume id == "+id);
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control",
                "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setContentType("application/octet-stream");
        String path = req.getSession().getServletContext().getRealPath("\\");
        String destFile="D:\\resume.doc";
        JsonResult jsonResult = resume.previewResume(String.valueOf(id));
        if(jsonResult.getData() != null) {
            Resume resume = (Resume) jsonResult.getData();
            Map<String,String> resumeMap = new HashMap<String,String>();
            resumeMap.put("title",resume.getTitle());
            resumeMap.put("name",resume.getRealName());
            response.setHeader("Content-disposition", "attachment; filename=\""
                    + URLEncoder.encode(resume.getTitle(), "UTF-8") + "\""); //
            resumeMap.put("sex",resume.getSex());
            resumeMap.put("marriage",resume.getMarriage());
            resumeMap.put("birthYear",resume.getBirthYear());
            resumeMap.put("education",resume.getEducation());
            resumeMap.put("liveArea",resume.getLiveArea());
            resumeMap.put("workYear",resume.getWorkYear());
            resumeMap.put("mobile",resume.getMobile());
            resumeMap.put("email",resume.getEmail());
            resumeMap.put("jobNature",resume.getJobNature());
            resumeMap.put("post",resume.getPost());
            resumeMap.put("jobArea",resume.getJobArea());
            resumeMap.put("hopeSalary",resume.getHopeSalary());
            resumeMap.put("status",resume.getStatus());
            resumeMap.put("eduExperience",resume.getEduExperience());
            resumeMap.put("jobExperience",resume.getJobExperience());
            resumeMap.put("projectExperience",resume.getProjectExperience());
            HWPFDocument document= ExporDoc.replaceDoc(path+"\\resume.doc", resumeMap);
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            try {
                document.write(ostream);
                //输出word文件
                /*OutputStream outs=new FileOutputStream(destFile);
                outs.write(ostream.toByteArray());*/
                ServletOutputStream stream = response.getOutputStream();
                stream.write(ostream.toByteArray());
                stream.flush();
                stream.close();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
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
     * @param createID
     * @param jobCity
     * @param name
     * @param pageNo
     * @param pageSize
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value="/rest/job/queryPublishPositionByID", method = RequestMethod.GET)
    public void queryPublishPositionByID(HttpServletRequest req, HttpServletResponse response,
                                   String createID,String city,String name,
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
        if(createId == null) {
            if (StringUtils.isEmpty(pageNo)) {
                pageNo = "1";
            }
            if (StringUtils.isEmpty(pageSize)) {
                pageSize = "10";
            }
            Paging<Resume> pager = new Paging<Resume>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title",title);
            map.put("jobCity",jobCity);
            map.put("expYear",expYear);
            map.put("education",education);
            jsonResult = resume.findAllResume(pager, map);
            jsonResult.setData(pager);
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
     * 查询推荐感兴趣的职位
     */
    @RequestMapping(value = "/rest/job/queryRecommendPosition", method = RequestMethod.GET)
    public void queryRecommendPosition(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        JsonResult jsonResult = new JsonResult();
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if(principal != null) {
                jsonResult = job.searchRecommendPosition(principal.getId().toString());
            }
        }else{
            jsonResult = job.searchLatestPublishPosition();
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }
}
