package com.zhuhuibao.business.oms.project;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.mybatis.oms.service.ProjectService;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.mybatis.oms.entity.ProjectInfo;

/**
 * Created by Administrator on 2016/4/11 0011.
 */
@Controller
public class ProjectController {
	 private static final Logger log = LoggerFactory.getLogger(ProjectController.class);
	 @Autowired
	 ProjectService projectService;
	 /**
     * 查询栏目信息详情
     * @param req
     * @param response
     * @param id  栏目信息的ID
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value="/rest/oms/queryProjectInfo", method = RequestMethod.GET)
    public void queryProjectInfo(HttpServletRequest req, HttpServletResponse response, ProjectInfo projectInfo) throws JsonGenerationException, JsonMappingException, IOException {
	    //封装查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", projectInfo.getId());
		map.put("name", projectInfo.getName());
		map.put("city", projectInfo.getCity());
		map.put("area", projectInfo.getArea());
		log.info("查询工程信息：queryProjectInfo",map);
		 JsonResult jsonResult = new JsonResult();
		List<ProjectInfo> projectList;
		try {
			//调用查询接口
			projectList = projectService.queryProjectInfoList(map);
			jsonResult.setData(projectList);
			jsonResult.setCode(200);
		} catch (SQLException e) {
			log.error("queryProjectInfo:query sql exception",e.getMessage());
			jsonResult.setMessage("查询项目工程信息失败！");
			jsonResult.setCode(400);
		} 
		 
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }
    
    /**
     * 添加项目信息
     * @param req
     * @param response
     * @param projectInfo  项目工程信息
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value="/rest/oms/addProjectInfo", method = RequestMethod.POST)
    public void addProjectInfo(HttpServletRequest req, HttpServletResponse response, ProjectInfo projectInfo) throws JsonGenerationException, JsonMappingException, IOException {
	   
		log.info("查询工程信息：queryProjectInfo",projectInfo);
		 JsonResult jsonResult = new JsonResult();
		int reslult=0;
		try {
			//添加项目信息
			reslult = projectService.addProjectInfo(projectInfo);
		} catch (SQLException e) {
			log.error("queryProjectInfo:query sql exception",e.getMessage());
			jsonResult.setCode(400);
			jsonResult.setMessage("添加项目工程信息失败！");
			
		} 
		if(reslult==0){
			jsonResult.setCode(400);
			jsonResult.setMessage("添加项目工程失败！");
        }else{
        	jsonResult.setCode(200);
        }
		
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }
    
    /**
     * 修改项目信息
     * @param req
     * @param response
     * @param projectInfo  项目工程信息
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value="/rest/oms/updateProjectInfo", method = RequestMethod.POST)
    public void updateProjectInfo(HttpServletRequest req, HttpServletResponse response, ProjectInfo projectInfo) throws JsonGenerationException, JsonMappingException, IOException {
	   
		log.info("修改工程信息：updateProjectInfo",projectInfo);
		 JsonResult jsonResult = new JsonResult();
		int reslult=0;
		try {
			//修改项目信息
			reslult = projectService.updateProjectInfo(projectInfo);
		} catch (SQLException e) {
			log.error("updateProjectInfo:update sql exception",e.getMessage());
			jsonResult.setCode(400);
			jsonResult.setMessage("修改项目工程信息失败！");
			
		} 
		if(reslult==0){
			jsonResult.setCode(400);
			jsonResult.setMessage("修改项目工程失败！");
        }else{
        	jsonResult.setCode(200);
        }
		
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

}
