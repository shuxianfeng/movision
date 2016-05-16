package com.zhuhuibao.business.oms.project;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.oms.service.ProjectLinkmanService;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.oms.service.ProjectService;
import com.zhuhuibao.mybatis.oms.entity.ProjectInfo;

/**
 * Created by Administrator on 2016/4/11 0011.
 */
@RestController
@RequestMapping(value="/rest/project/")
@Api(value="项目信息",description = "项目信息")
public class ProjectController {
	 private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

	 @Autowired
	 ProjectService projectService;

	 @Autowired
	 private MemberService memberService;

	 /**
     * 根据条件查询项目分页信息
     * @param name  项目信息
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value="searchProjectPage", method = RequestMethod.GET)
	@ApiOperation(value = "根据条件查询项目分页信息",notes = "根据条件查询分页",response = JsonResult.class)
    public JsonResult searchProjectPage(@ApiParam(value = "项目名称") @RequestParam(required = false) String name,
										@ApiParam(value = "城市Code") @RequestParam(required = false) String city,
										@ApiParam(value = "省代码") @RequestParam(required = false) String province,
										@ApiParam(value = "项目类别") @RequestParam(required = false) String category,
										@ApiParam(value = "开工日期查询开始日期") @RequestParam(required = false) String startDateA,
										@ApiParam(value = "开工日期查询结束日期") @RequestParam(required = false) String startDateB,
										@ApiParam(value = "竣工日期查询开始日期") @RequestParam(required = false) String endDateA,
										@ApiParam(value = "竣工日期查询结束日期") @RequestParam(required = false) String endDateB,
									   @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
									   @ApiParam(value="每页显示的条数") @RequestParam(required = false) String pageSize) throws Exception {
	    //封装查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("city", city);
		map.put("province", province);
		map.put("category",category);
		map.put("startDateA",startDateA);
		map.put("startDateB",startDateB);
		map.put("endDateA",endDateA);
		map.put("endDateB",endDateB);
		log.info("查询工程信息：queryProjectInfo",map);
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		if (StringUtils.isEmpty(pageSize)) {
			pageSize = "10";
		}
		Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
		//调用查询接口
		List<Map<String,String>> projectList = projectService.findAllPrjectPager(map,pager);
		pager.result(projectList);
		jsonResult.setData(pager);
		return jsonResult;
    }
    
    /**
     * 添加项目信息
     * @param json  项目工程信息
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value="addProjectInfo", method = RequestMethod.POST)
	@ApiOperation(value = "运营后台添加项目信息",notes = "运营后台的新增项目",response = JsonResult.class)
    public JsonResult addProjectInfo(@ApiParam(value = "项目信息+甲方乙方信息") String json) throws JsonGenerationException, JsonMappingException, IOException {
	   
		log.info("查询工程信息：queryProjectInfo",json);
		JsonResult jsonResult = new JsonResult();
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
			jsonResult.setCode(400);
			jsonResult.setMessage("添加项目工程信息失败！");
			
		} 
		if(reslult==0){
			jsonResult.setCode(400);
			jsonResult.setMessage("添加项目工程失败！");
        }else{
        	jsonResult.setCode(200);
        }
		return jsonResult;
    }
    
    /**
     * 修改项目信息
     * @param projectInfo  项目工程信息
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value="updateProjectInfo", method = RequestMethod.POST)
	@ApiOperation(value = "运营后台修改项目信息",notes = "运营后台的修改项目信息",response = JsonResult.class)
    public JsonResult updateProjectInfo(@ApiParam(value = "项目信息+甲方乙方信息") @ModelAttribute() ProjectInfo projectInfo) throws JsonGenerationException, JsonMappingException, IOException {
	   
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
		return jsonResult;
    }

	@RequestMapping(value = "previewProject",method = RequestMethod.GET)
	@ApiOperation(value="项目信息详情",notes = "根据Id查看项目信息",response = JsonResult.class)
	public JsonResult previewProject(@ApiParam(value = "项目信息ID") @RequestParam Long porjectID) throws Exception {
		JsonResult jsonResult = new JsonResult();
		Map<String,Object> map  = projectService.queryProjectDetail(porjectID);
		jsonResult.setData(map);
		return jsonResult;
	}

	@RequestMapping(value = "previewUnLoginProject",method = RequestMethod.GET)
	@ApiOperation(value="预览未登陆的项目信息",notes = "根据Id查看未登陆的项目信息",response = JsonResult.class)
	public JsonResult previewUnLoginProject(@ApiParam(value = "项目信息ID") @RequestParam Long porjectID) throws Exception {
		JsonResult jsonResult = new JsonResult();
		Map<String,Object> map  = projectService.previewUnLoginProject(porjectID);
		jsonResult.setData(map);
		return jsonResult;
	}

	@RequestMapping(value = "greatCompany", method = RequestMethod.GET)
	@ApiOperation(value = "优秀工程商",notes = "优秀工程商",response = JsonResult.class)
	public JsonResult greatCompany(@ApiParam("工程商类型=2") @RequestParam String type) throws IOException {
		JsonResult jsonResult = memberService.greatCompany(type);
		return jsonResult;
	}

	@RequestMapping(value = "queryLatestProject", method = RequestMethod.GET)
	@ApiOperation(value = "查询最新项目信息，默认10条",notes = "查询最新项目信息，默认10条",response = JsonResult.class)
	public JsonResult queryLatestProject() throws IOException {
		JsonResult jsonResult = new JsonResult();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("count",10);
		List<ProjectInfo> projectList = projectService.queryLatestProject(map);
		jsonResult.setData(projectList);
		return jsonResult;
	}

}
