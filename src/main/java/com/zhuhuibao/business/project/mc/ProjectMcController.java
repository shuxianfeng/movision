package com.zhuhuibao.business.project.mc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.project.service.ProjectService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * 会员中心我查看过的项目信息
 *
 * @author pl
 * @create 2016/6/1 0001
 **/
@RestController
@RequestMapping(value = "/rest/project/mc")
@Api(value = "projectMc", description = "会员中心我查看过的项目信息")
public class ProjectMcController {
	@Autowired
	ProjectService projectService;

	/**
	 * 根据条件查询项目分页信息
	 */
	@RequestMapping(value = { "sel_project_info", "base/sel_project_info" }, method = RequestMethod.GET)
	@ApiOperation(value = "查询我查看过的项目信息", notes = "查询我查看过的项目信息", response = Response.class)
	public Response searchProjectPage(@ApiParam(value = "项目名称") @RequestParam(required = false) String name,
			@ApiParam(value = "城市Code") @RequestParam(required = false) String city, @ApiParam(value = "省代码") @RequestParam(required = false) String province,
			@ApiParam(value = "项目类别") @RequestParam(required = false) String category, @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
			@ApiParam(value = "每页显示的条数") @RequestParam(required = false) String pageSize) throws Exception {
		// 封装查询参数
		Long createId = ShiroUtil.getCreateID();
		Response response = new Response();
		if (createId != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (name != null && !"".equals(name)) {
				map.put("name", name.replace("_", "\\_"));
			}
			map.put("city", city);
			map.put("province", province);
			map.put("category", category);
			map.put("viewerId", createId);
			map.put("type", ZhbConstant.ZhbGoodsType.CKXMXX.toString());
			if (StringUtils.isEmpty(pageNo)) {
				pageNo = "1";
			}
			if (StringUtils.isEmpty(pageSize)) {
				pageSize = "10";
			}
			Paging<Map<String, String>> pager = new Paging<Map<String, String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
			// 调用查询接口
			List<Map<String, String>> projectList = projectService.queryOmsViewProject(map, pager);
			pager.result(projectList);
			response.setData(pager);
		} else {
			throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
		}
		return response;
	}

	@RequestMapping(value = { "deal_project_info" }, method = RequestMethod.GET)
	@ApiOperation(value = "查询我查看过的项目信息", notes = "查询我查看过的项目信息", response = Response.class)
	public Response dealProjectPage() throws Exception {
		long startTime = (new Date()).getTime();
		Response response = new Response();
		String curAccount = ShiroUtil.getMember().getAccount();

		int total = 0;
		int succ = 0;
		int failed = 0;
		if ("tongxl@zhuhui8.com".equals(curAccount)) {
			List<Map<String, Object>> allList = projectService.findAllPrjectCopy();
			if (CollectionUtils.isNotEmpty(allList)) {
				total = allList.size();
				for (Map<String, Object> m : allList) {
					boolean result = projectService.dealProjectPage(m);
					if (result) {
						succ++;
					} else {
						failed++;
					}
				}
			}
		}
		long endTime = (new Date()).getTime();
		long expTime = (endTime - startTime);
		long extTimeSec = expTime / 1000;
		String a = "total:" + total + ",succ:" + succ + ",failed:" + failed + ",expTime:" + expTime + ",extTimeSec:" + extTimeSec;
		response.setData(a);
		return response;
	}

}
