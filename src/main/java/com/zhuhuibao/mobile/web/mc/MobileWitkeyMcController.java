package com.zhuhuibao.mobile.web.mc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.PageNotFoundException;
import com.zhuhuibao.mybatis.constants.service.ConstantService;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.witkey.entity.Cooperation;
import com.zhuhuibao.mybatis.witkey.service.CooperationService;
import com.zhuhuibao.service.MobileWitkeyService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

@RestController
@RequestMapping("/rest/m/witkey/mc/")	
public class MobileWitkeyMcController {
	
	@Autowired
	private CooperationService cooperationService;
	
	@Autowired
	MobileWitkeyService mWitkeySV;
	
	@Autowired
	private ConstantService constantService;
	
	@ApiOperation(value = "查询我发布的任务（后台分页）", notes = "查询我发布的任务（后台分页）", response = Response.class)
	@RequestMapping(value = "sel_myWitkeyList", method = RequestMethod.GET)
	public Response findAllMyCooperationByPager(
			@RequestParam(required = false) String pageNo,
			@RequestParam(required = false) String pageSize,
			@ApiParam(value = "合作标题") @RequestParam(required = false) String title,
			@ApiParam(value = "合作类型") @RequestParam(required = false) String type,
			@ApiParam(value = "审核状态") @RequestParam(required = false) String status) {
		
		return mWitkeySV.findAllMyCooperationByPager(pageNo, pageSize, title, type, status);
		
	}
	
	
	@ApiOperation(value = "发布任务,/发布威客服务，/发布威客资质合作", notes = "发布任务,/发布威客服务，/发布威客资质合作", response = Response.class)
    @RequestMapping(value = "add_witkey", method = RequestMethod.POST)
    public Response publishCooperation(@ApiParam(value = "威客信息") @ModelAttribute(value = "cooperation") Cooperation cooperation) throws Exception {
		
        return mWitkeySV.publishCooperation(cooperation);
    }
	
	
	@ApiOperation(value = "威客信息詳情", notes = "威客信息詳情", response = Cooperation.class)
    @RequestMapping(value = "sel_witkey", method = RequestMethod.GET)
    public Response cooperationInfo(@RequestParam String id) {
        
		return mWitkeySV.cooperationInfo(id);
    }
	
	@ApiOperation(value = "批量删除任务", notes = "批量删除任务", response = Response.class)
	@RequestMapping(value = "del_witkey", method = RequestMethod.POST)
	public Response deleteCooperation(@RequestParam String ids[]) {
		Response response = new Response();
		cooperationService.deleteCooperation(ids);
		return response;
	}
	
	@ApiOperation(value = "我查看的威客任务", notes = "我查看的威客任务", response = Response.class)
	@RequestMapping(value = "sel_witkey_task", method = RequestMethod.GET)
	public Response sel_witkey_task(@RequestParam(required = false) String pageNo,
			@RequestParam(required = false) String pageSize,
			@ApiParam(value = "合作标题") @RequestParam(required = false) String title,
			@ApiParam(value = "合作类型") @RequestParam(required = false) String type) {
		
		return mWitkeySV.sel_witkey_task(pageNo, pageSize, title, type);
	}
	
	@ApiOperation(value = "批量删除我查看的威客任务", notes = "批量删除我查看的威客任务", response = Response.class)
	@RequestMapping(value = "del_witkey_task", method = RequestMethod.POST)
	public Response del_witkey_task(@RequestParam String ids) {
		
		return mWitkeySV.del_witkey_task(ids);
	}
	
	@ApiOperation(value = "合作类型(子类)", notes = "合作类型(子类)", response = Response.class)
	@RequestMapping(value = "sel_subWitkeyTypeList", method = RequestMethod.GET)
	public Response subCooperationType() {
		Response response = new Response();
		List list = cooperationService.subCooperationType();
		response.setData(list);
		return response;
	}
	
	/**
	 * 项目类别
	 * 发布任务时，任务类型为设计任务时，项目类别的数据获取接口
	 */
	@ApiOperation(value = "项目类别", notes = "项目类别", response = Response.class)
	@RequestMapping(value = "sel_witkeyCategoryList", method = RequestMethod.GET)
	public Response cooperationCategory() {
		Response response = new Response();
		// 项目类别type为9
		String type = "9";
		List<Map<String, String>> list = constantService.findByType(type);
		response.setData(list);
		return response;
	}
	
	@ApiOperation(value = "发布任务时，自动获取发布者的联系方式", notes = "发布任务时，自动获取发布者的联系方式", response = Response.class)
    @RequestMapping(value = "sel_connection", method = RequestMethod.GET)
    public Response sel_connection() throws Exception {
        
		return mWitkeySV.sel_connection();
    }
	
	
	
	
}
