package com.zhuhuibao.business.project.oms;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.project.entity.ProjectInfo;
import com.zhuhuibao.mybatis.project.service.ProjectService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/11 0011.
 */
@RestController
@RequestMapping(value="/rest/project")
@Api(value="ProjectOms",description = "项目信息")
public class ProjectOmsController {
	 private static final Logger log = LoggerFactory.getLogger(ProjectOmsController.class);

	 @Autowired
	 ProjectService projectService;

	 @Autowired
	 private MemberService memberService;

    /**
     * 添加项目信息
     * @param json  项目工程信息
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value={"addProjectInfo","oms/base/add_projectInfo"}, method = RequestMethod.POST)
	@ApiOperation(value = "运营后台添加项目信息",notes = "运营后台的新增项目",response = Response.class)
    public Response addProjectInfo(@ApiParam(value = "项目信息+甲方乙方信息") String json) throws JsonGenerationException, JsonMappingException, IOException {
	   
		log.info("查询工程信息：queryProjectInfo",json);
		Response response = new Response();
		int reslult=0;
		try {
			//添加项目信息
			Long createId = ShiroUtil.getOmsCreateID();
			if(createId != null) {
				Gson gson = new Gson();
				ProjectInfo projectInfo = gson.fromJson(json, ProjectInfo.class);
				projectInfo.setCreateid(createId);
				reslult = projectService.addProjectInfo(projectInfo);
			}
		} catch (SQLException e) {
			log.error("queryProjectInfo:query sql exception",e.getMessage());
			response.setCode(400);
			response.setMessage("添加项目工程信息失败！");
			
		} 
		if(reslult==0){
			response.setCode(400);
			response.setMessage("添加项目工程失败！");
        }else{
        	response.setCode(200);
        }
		return response;
    }
    
    /**
     * 修改项目信息
     * @param json  项目工程信息
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value={"updateProjectInfo","oms/base/upd_projectInfo"}, method = RequestMethod.POST)
	@ApiOperation(value = "运营后台修改项目信息",notes = "运营后台的修改项目信息",response = Response.class)
    public Response updateProjectInfo(@ApiParam(value = "项目信息+甲方乙方信息") String json) throws JsonGenerationException, JsonMappingException, IOException {
    	Gson gson = new Gson();
		ProjectInfo projectInfo = gson.fromJson(json, ProjectInfo.class);
		  
		log.info("修改工程信息：updateProjectInfo",projectInfo);
		Response response = new Response();
		int reslult=0;
		try {
			//修改项目信息
			reslult = projectService.updateProjectInfo(projectInfo);
		} catch (SQLException e) {
			log.error("updateProjectInfo:update sql exception",e.getMessage());
			response.setCode(400);
			response.setMessage("修改项目工程信息失败！");
			
		} 
		if(reslult==0){
			response.setCode(400);
			response.setMessage("修改项目工程失败！");
        }else{
        	response.setCode(200);
        }
		return response;
    }

	@RequestMapping(value = {"previewOmsProject","oms/base/sel_omsProject"},method = RequestMethod.GET)
	@ApiOperation(value="项目信息详情",notes = "根据Id查看项目信息",response = Response.class)
	public Response previewOmsProject(@ApiParam(value = "项目信息ID") @RequestParam Long porjectID) throws Exception {
		Response response = new Response();
		Map<String,Object> map  = projectService.queryOmsProjectDetail(porjectID);
		response.setData(map);
		return response;
	}

}
