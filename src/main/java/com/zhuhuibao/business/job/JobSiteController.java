package com.zhuhuibao.business.job;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.mybatis.sitemail.entity.MessageText;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
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
    SiteMailService smService;

    @RequestMapping(value="/rest/job/applyPosition", method = RequestMethod.GET)
    public void queryCompanyInfo(HttpServletRequest req, HttpServletResponse response,MessageText msgText) throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("query company info id "+msgText.getMessageText());
        JsonResult jsonResult = smService.addSiteMail(msgText);
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
    public void queryOtherPosition(HttpServletRequest req, HttpServletResponse response,Long jobID,Long createID,String pageNo,String pageSize) throws JsonGenerationException, JsonMappingException, IOException
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
        JsonResult jsonResult = job.queryOtherPosition(pager,map);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    @RequestMapping(value="/rest/job/queryAllPosition", method = RequestMethod.GET)
    public void queryAllPosition(HttpServletRequest req, HttpServletResponse response,
                                 String name,String enterpriseName,String province,String city,String area,String employeeNumber,String enterpriseType,
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
        map.put("name",name);
        map.put("enterpriseName",enterpriseName);
        map.put("province",province);
        map.put("city",city);
        map.put("area",area);
        map.put("employeeNumber",employeeNumber);
        map.put("enterpriseType",enterpriseType);
        JsonResult jsonResult = job.queryOtherPosition(pager,map);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }
}
