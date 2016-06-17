package com.zhuhuibao.business.oms.platform;

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

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.oms.entity.PlatformStatistics;
import com.zhuhuibao.mybatis.oms.service.PlatformStatService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * 统计接口
 * Created by gmli on 2016/6/13 0004.
 */
@RestController
@RequestMapping("/rest/stat/oms")
public class PlatformStatisticsController {
	 private static final Logger log = LoggerFactory.getLogger(PlatformStatisticsController.class);
	 @Autowired
	 private PlatformStatService platformStatService;
	 
	    /**
	     * 平台信息统计
	     * @author gmli
	     * @since 2016.5.26 
	     */
	    @ApiOperation(value="平台信息统计",notes="平台信息统计",response = Response.class)
	    @RequestMapping(value = "sel_platform_stat", method = RequestMethod.GET)
	    public Response queryPlatformStatistics() {
	    	  Response Response = new Response(); 
	          //查询
	    	  Map<String,List<PlatformStatistics>> platformStatisticsList = platformStatService.findAllPlatformStat();
	        
	          Response.setData(platformStatisticsList);
	          return Response;
	    }
	    /**
	     * 待处理数据统计
	     * @author gmli
	     * @since 2016.5.26 
	     */
	    @ApiOperation(value="平台信息统计",notes="平台信息统计",response = Response.class)
	    @RequestMapping(value = "sel_platform_waitstat", method = RequestMethod.GET)
	    public Response queryPlatformWaitStatistics() {
	    	  Response Response = new Response(); 
	          //查询待处理的数据
	    	  Map<String,String> waitStatList = platformStatService.findAllPlatformWaitStat();
	          Response.setData(waitStatList);
	          return Response;
	    }
}
