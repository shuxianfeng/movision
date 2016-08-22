package com.zhuhuibao.business.oms.ad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.ad.service.AdService;
import com.zhuhuibao.mybatis.expo.entity.Exhibition;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * 广告管理
 * @author gmli 2016.08.03
 *
 */
@RestController
@RequestMapping("/rest/ad/oms")
@Api(value="AdManger")
public class AdController {
	
	 private static final Logger log = LoggerFactory.getLogger(AdController.class);

	    @Autowired
	    private AdService adService;

	    @ApiOperation(value="广告(运营分页)",notes="广告列表(运营分页)",response = Response.class)
	    @RequestMapping(value = "sel_adList", method = RequestMethod.GET)
	    public Response queryAdList(@ApiParam(value = "广告频道类型")@RequestParam(required = false) String chanType,
	                                       @ApiParam(value = "平道子页面")@RequestParam(required = false)String chanPage,
	                                       @ApiParam(value = "广告位区域")@RequestParam(required = false) String advArea,
	                                       @ApiParam(value = "广告类型")@RequestParam(required = false)String advType,
	                                       @RequestParam(required = false)String pageNo,
	                                       @RequestParam(required = false)String pageSize,
	                                       @ApiParam(value = "修改类型")@RequestParam(required = false) String type)  {
	        Response response = new Response();
	        //设定默认分页pageSize
	        if (StringUtils.isEmpty(pageNo)) {
	            pageNo = "1";
	        }
	        if (StringUtils.isEmpty(pageSize)) {
	            pageSize = "10";
	        }
	        Paging pager = new Paging(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
	        Map<String,Object> map = new HashMap<>();
	        //查询传参+
	        map.put("chanType",chanType);
	        map.put("chanPage",chanPage);
	        map.put("advArea",advArea);
	        map.put("advType",advType);
	        map.put("type",type);
	        List<Map<String, String>> adList = adService.findAllAdList(pager,map);
	        pager.result(adList);
	        response.setData(pager);
	        return response;
	    }
	    
	    @RequestMapping(value = "sel_adType", method = RequestMethod.GET)
	    @ApiOperation(value = "根据类型查询", notes = "根据类型查询", response = Response.class)
	    public Response findByType(@ApiParam(value = "广告频道类型")@RequestParam(required = false) String chanType,
                @ApiParam(value = "平道子页面")@RequestParam(required = false)String chanPage,
                @ApiParam(value = "广告位区域")@RequestParam(required = false) String advArea) {
	        Response result = new Response();
	        try {
	        	  Map<String,Object> map = new HashMap<>();
	  	        //查询传参+
	  	        map.put("chanType",chanType);
	  	        map.put("chanPage",chanPage);
	  	        map.put("advArea",advArea); 
	  	        
	            List<Map<String,String>> list = adService.findAdType(map);
	            result.setData(list);
	        } catch (Exception e) {
	            e.printStackTrace();
	            log.error(e.getMessage());
	        }

	        return result;
	    }
	    
	    @RequestMapping(value = "upd_AdInfo", method = RequestMethod.POST)
	    @ApiOperation(value = "修改广告信息", notes = "修改广告信息", response = Response.class)
	    public Response updateAdInfo(@ApiParam(value = "广告ID")@RequestParam(required = false) String id,
                @ApiParam(value = "名称")@RequestParam(required = false)String name,
                @ApiParam(value = "排序")@RequestParam(required = false) String sort,
                @ApiParam(value = "频道类型")@RequestParam(required = false) String chanType,
                @ApiParam(value = "频道名称")@RequestParam(required = false) String chanPage,
                @ApiParam(value = "广告类别")@RequestParam(required = false) String advType,
                @ApiParam(value = "所属上级广告")@RequestParam(required = false) String parentId,
                @ApiParam(value = "广告位置")@RequestParam(required = false) String position,
                @ApiParam(value = "广告标题")@RequestParam(required = false) String title,
                @ApiParam(value = "广告链接路径")@RequestParam(required = false) String linkUrl,
                @ApiParam(value = "广告位区域")@RequestParam(required = false) String advArea,
                @ApiParam(value = "广告图片路径")@RequestParam(required = false) String imgUrl,
                @ApiParam(value = "广告位置")@RequestParam(required = false) String connectedId, 
                @ApiParam(value = "广告有效开始")@RequestParam(required = false) String startDate,
                @ApiParam(value = "广告有效结束时间")@RequestParam(required = false) String endDate,
                @ApiParam(value = "修改类型")@RequestParam(required = false) String type) {
	        Response result = new Response();
	 
	        try {
	        	
	        	 Long createId = ShiroUtil.getOmsCreateID();
	        	 //判断用户是否登陆
	 	        if(createId!=null){
	 	        	Map<String,Object> map = new HashMap<>();
	 	  	        //修改参数
	 	  	        map.put("id",id);
	 	  	        map.put("name",name);
	 	  	        map.put("sort",sort); 
	 	  	        map.put("type",type); 
	 	  	        map.put("chanType",chanType); 
	 	  	        map.put("chanPage",chanPage); 
	 	  	        map.put("advType",advType); 
	 	  	        map.put("parentId",parentId); 
	 	  	        map.put("position",position); 
	 	  	        map.put("title",title);
	 	  	        map.put("linkUrl",linkUrl);
	 	  	        map.put("imgUrl",imgUrl);
	 	  	     	map.put("connectedId",connectedId);
	 	  	        map.put("operatorId",createId); 
	 	  	        map.put("advArea",advArea);
	 	  	        map.put("startDate",startDate); 
	 	  	     	map.put("endDate",endDate); 
	 	  	       if("5".equals(type))
	 	  	        {
	 	  	           adService.updateAdDetails(map);
	 	  	        }else{
	 	  	        	adService.updateAdInfo(map);
	 	  	        }
	 	  	        
	 	        }else {
	 	            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
	 	        }
	        	 
	          
	        } catch (Exception e) {
	            e.printStackTrace();
	            log.error("update Ad error>>>{}", e);
	           
	        }

	        return result;
	    }
	    
	    @RequestMapping(value = "upd_add_AdInfo", method = RequestMethod.POST)
	    @ApiOperation(value = "添加广告信息", notes = "添加广告信息", response = Response.class)
	    public Response addAdInfo(
                @ApiParam(value = "名称")@RequestParam(required = false)String name,
                @ApiParam(value = "排序")@RequestParam(required = false) String sort,
                @ApiParam(value = "频道类型")@RequestParam(required = false) String chanType,
                @ApiParam(value = "频道名称")@RequestParam(required = false) String chanPage,
                @ApiParam(value = "广告类别")@RequestParam(required = false) String advType,
                @ApiParam(value = "所属上级广告")@RequestParam(required = false) String parentId,
                @ApiParam(value = "广告位置")@RequestParam(required = false) String position,
                @ApiParam(value = "广告标题")@RequestParam(required = false) String title,
                @ApiParam(value = "广告链接路径")@RequestParam(required = false) String linkUrl,
                @ApiParam(value = "广告图片路径")@RequestParam(required = false) String imgUrl,
                @ApiParam(value = "广告位区域")@RequestParam(required = false) String advArea,
                @ApiParam(value = "广告位置")@RequestParam(required = false) String connectedId, 
                @ApiParam(value = "广告有效开始")@RequestParam(required = false) String startDate,
                @ApiParam(value = "广告有效结束时间")@RequestParam(required = false) String endDate,
                @ApiParam(value = "添加类型")@RequestParam(required = false) String type) {
	        Response result = new Response();
	 
	        try {
	        	
	        	 Long createId = ShiroUtil.getOmsCreateID();
	        	 //判断用户是否登陆
	 	        if(createId!=null){
	 	        	Map<String,Object> map = new HashMap<>();
	 	  	        //修改参数 
	 	  	        map.put("name",name);
	 	  	        map.put("sort",sort==""?null:sort); 
	 	  	        map.put("type",type); 
	 	  	        map.put("chanType",chanType); 
	 	  	        map.put("chanPage",chanPage); 
	 	  	        map.put("advType",advType); 
	 	  	        map.put("parentId",parentId); 
	 	  	        map.put("position",position); 
	 	  	        map.put("title",title);
	 	  	        map.put("linkUrl",linkUrl);
	 	  	        map.put("imgUrl",imgUrl);
	 	  	     	map.put("connectedId",connectedId);
	 	  	        map.put("advArea",advArea); 
	 	  	        map.put("operatorId",createId);
	 	  	        map.put("startDate",startDate); 
	 	  	     	map.put("endDate",endDate); 
	 	  	        if("5".equals(type))
	 	  	        {
	 	  	        	adService.addAdDetails(map);
	 	  	        }else{
	 	  	        	adService.addAdInfo(map);
	 	  	        }
	 	  	        
	 	        }else {
	 	            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
	 	        }
	        	 
	          
	        } catch (Exception e) {
	            e.printStackTrace();
	            log.error("add addAdInfo error>>>{}", e);
	           
	        }

	        return result;
	    }
	    
	    /**
	     * 会展信息删除
	     */
	    @ApiOperation(value="删除广告详情",notes="删除广告详情",response = Response.class)
	    @RequestMapping(value = "del_AdvDetails", method = RequestMethod.POST)
	    public Response deleteAdvDetails(@ApiParam(value = "id") @RequestParam String id)  {
	        Response response = new Response();
	       
	        int result=adService.deleteAdvDetails(id);
	        if(result>0)
	        {
	        	response.setCode(200);
	        	response.setMessage("删除成功");
	        }else{
	        	 response.setCode(400);
	        	 response.setMessage("删除失败");
	        }
	       
	        return response;
	    }
	    
	    @ApiOperation(value="广告(运营分页)",notes="广告列表(运营分页)",response = Response.class)
	    @RequestMapping(value = "sel_IdExits", method = RequestMethod.GET)
	    public Response queryIdExits(@ApiParam(value = "关联ID")@RequestParam(required = false) String id,
	                                       @ApiParam(value = "关联类型")@RequestParam(required = false)String method
	                                        )  {
	        Response response = new Response(); 
	        Map<String,Object> map = new HashMap<>();
	        //查询传参+
	        map.put("id",id);
	        map.put("method",method); 
	        int result = adService.queryIdExits(map);
	        if(result>0){
	        	response.setCode(200);
	        }else{
	        	response.setCode(400);
	        	response.setMessage("关联ID不存在");
	        }
	       
	        return response;
	    }
}
