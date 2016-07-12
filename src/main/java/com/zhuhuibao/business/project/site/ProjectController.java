package com.zhuhuibao.business.project.site;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.fsearch.pojo.spec.ProjectSearchSpec;
import com.zhuhuibao.fsearch.service.impl.ProjectFSService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.project.service.ProjectService;
import com.zhuhuibao.service.payment.PaymentService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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

    @Autowired
    ProjectFSService projectfsService;

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
		Paging<Map<String,String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
		//调用查询接口
		List<Map<String,String>> projectList =   projectService.findAllProject(map,pager);
				//projectService.findAllPrjectPager(map,pager);
		pager.result(projectList);
		response.setData(pager);
		return response;
    }


	@RequestMapping(value={"searchProjectPage1","site/base/sel_project_page"}, method = RequestMethod.GET)
	@ApiOperation(value = "根据条件查询项目信息(分页)",notes = "根据条件查询项目信息(分页)",response = Response.class)
	public Response searchProjectPage(@ApiParam("搜索条件")  @ModelAttribute ProjectSearchSpec spec){
        if (spec.getLimit() <= 0 || spec.getLimit() > 100) {
            spec.setLimit(12);
        }
        Response response = new Response();
        response.setCode(200);
        Map<String, Object> ret;
        try{
            ret = projectfsService.searchProjectPage(spec);
            response.setMsgCode(1);
            response.setMessage("OK!");
            response.setData(ret);
        }
        catch (Exception e) {
            response.setMsgCode(0);
            response.setMessage("search error!");
        }
		return response;
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
    public Response sel_great_manufacturer(@ApiParam(value="频道类型 4：项目")@RequestParam String chanType,
                                           @ApiParam(value="频道下子页面:index") @RequestParam String page,
                                           @ApiParam(value="广告所在区域:F1:优秀工程商") @RequestParam String advArea)  {
        Response response = new Response();
        Map<String,Object> map = new HashMap();
        map.put("chanType",chanType);
        map.put("page",page);
        map.put("advArea",advArea);
        List<Map<String,String>> list = memberService.queryGreatCompany(map);
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
