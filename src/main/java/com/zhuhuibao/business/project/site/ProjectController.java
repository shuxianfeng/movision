package com.zhuhuibao.business.project.site;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.service.payment.PaymentService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zhuhuibao.mybatis.project.service.ProjectService;

/**
 * Created by Administrator on 2016/4/11 0011.
 */
@RestController
@RequestMapping(value="/rest/project")
@Api(value="Project",description = "项目信息")
public class ProjectController {
	 private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

	 @Autowired
	 ProjectService projectService;

	 @Autowired
	 private MemberService memberService;

	@Autowired
	PaymentService paymentService;

	 /**
     * 根据条件查询项目分页信息
     * @param name  项目信息
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value={"searchProjectPage","site/base/sel_projectPage"}, method = RequestMethod.GET)
	@ApiOperation(value = "根据条件查询项目分页信息",notes = "根据条件查询分页",response = Response.class)
    public Response searchProjectPage(@ApiParam(value = "项目名称") @RequestParam(required = false) String name,
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
		if(name != null && !"".equals(name))
		{
			map.put("name",name.replace("_","\\_"));
		}
		map.put("city", city);
		map.put("province", province);
		map.put("category",category);
		map.put("startDateA",startDateA);
		map.put("startDateB",startDateB);
		map.put("endDateA",endDateA);
		map.put("endDateB",endDateB);
		log.info("查询工程信息：queryProjectInfo",map);
		Response response = new Response();
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
		response.setData(pager);
		return response;
    }
    
	@RequestMapping(value = {"previewProject","site/base/sel_project"},method = RequestMethod.GET)
	@ApiOperation(value="项目信息详情",notes = "根据Id查看项目信息",response = Response.class)
	//    @ZhbAutoPayforAnnotation(goodsType=ZhbGoodsType.CKJSCG)
	public Response previewProject(@ApiParam(value = "项目信息ID") @RequestParam Long porjectID) throws Exception {
		Map<String,Object> map  = projectService.queryProjectDetail(porjectID);
		return paymentService.viewGoodsRecord(porjectID,map,"project");
	}

	@RequestMapping(value = {"previewUnLoginProject","site/base/sel_unLoginProject"},method = RequestMethod.GET)
	@ApiOperation(value="预览未登陆的项目信息",notes = "根据Id查看未登陆的项目信息",response = Response.class)
	public Response previewUnLoginProject(@ApiParam(value = "项目信息ID") @RequestParam Long porjectID) throws Exception {
		Response response = new Response();
		Map<String,Object> map  = projectService.previewUnLoginProject(porjectID);
		response.setData(map);
		return response;
	}

	@RequestMapping(value = {"greatCompany","site/base/sel_greatCompany"}, method = RequestMethod.GET)
	@ApiOperation(value = "优秀工程商",notes = "优秀工程商",response = Response.class)
	public Response greatCompany(@ApiParam("工程商类型=2") @RequestParam String type) throws Exception {
		Response response = new Response();
		List list = memberService.greatCompany(type);
		response.setData(list);
		return response;
	}

	@RequestMapping(value = {"queryLatestProject","site/base/sel_latestProject"}, method = RequestMethod.GET)
	@ApiOperation(value = "查询最新项目信息或者搜索时的推荐，默认10条",notes = "查询最新项目信息，默认10条",response = Response.class)
	public Response queryLatestProject() throws IOException {
		Response response = new Response();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("count",10);
		List<Map<String,String>> projectList = projectService.queryLatestProject(map);
		response.setData(projectList);
		return response;
	}
}
