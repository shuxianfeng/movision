package com.zhuhuibao.business.oms.platform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.sitemail.entity.MessageText;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.oms.entity.Notice;
import com.zhuhuibao.mybatis.oms.service.NoticeService;
import com.zhuhuibao.shiro.realm.OMSRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * 平台公告管理
 * @author Administrator
 * @since 2016.5.26
 *
 */
@RestController
@RequestMapping("/rest/notice/oms")
@Api(value="PlatForm")
public class NoticeController {
	 @Autowired
	 private  NoticeService noticeService;

	    /**
	     * 投诉建议管理
	     * @author gmli
	     * @since 2016.5.26 
	     */
	    @ApiOperation(value="平台公告查询",notes="平台公告查询",response = Response.class)
	    @RequestMapping(value = "sel_platform_notice", method = RequestMethod.GET)
	    public Response queryPlatformNotice(@ApiParam(value = "标题")@RequestParam(required = false) String title,
	            @ApiParam(value = "状态")@RequestParam(required = false)String status,
	    		@RequestParam(required = false)String pageNo,
	            @RequestParam(required = false)String pageSize) {
	    	  Response Response = new Response();
	          //设定默认分页pageSize
	          if (StringUtils.isEmpty(pageNo)) {
	              pageNo = "1";
	          }
	          if (StringUtils.isEmpty(pageSize)) {
	              pageSize = "10";
	          }
	          Paging<Notice> pager = new Paging<Notice>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
	          Map<String,Object> map = new HashMap<>();
	          //查询传参
	          map.put("title",title);
	          map.put("status",status);
	          //查询
	          List<Notice> exhibitionList = noticeService.findAllPlatformNotice(pager,map);
	          pager.result(exhibitionList);
	          Response.setData(pager);
	          return Response;
	    }
	    /**
	     * 用户建议处理
	     * @author gmli
	     * @since 2016.5.26
	     */
	    @ApiOperation(value="用户建议处理",notes="用户建议处理",response = Response.class)
	    @RequestMapping(value = "upd_platform_notice", method = RequestMethod.POST)
	    public Response updatePlatformNotice(@ModelAttribute()Notice notice)  {
	        Response Response = new Response();
	        Subject currentUser = SecurityUtils.getSubject();
	        Session session = currentUser.getSession(false);
	        if(null != session) {
	            OMSRealm.ShiroOmsUser principal = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
	            if(null != principal){
	            	noticeService.updatePlatformNotice(notice);
	            }else{
	                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
	            }
	        }else{
	            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
	        }
	        return Response;
	    }
	    /**
	     * 查询用户建议详情
	     * @author gmli
	     * @since 2016.5.26
	     */
	    @ApiOperation(value="用户建议详情",notes="用户建议详情",response = Response.class)
	    @RequestMapping(value = "sel_platform_noticeById", method = RequestMethod.GET)
	    public Response queryPlatformNoticeById(@RequestParam Long id)  {
	        Response Response = new Response();
	        Notice notice = noticeService.queryPlatformNoticeById(id);
	        Response.setData(notice);
	        return Response;
	    }
	    
	    /**
	     * 发布公告信息
	     * @author gmli
	     * @since 2016.5.26 
	     */
	    @ApiOperation(value="发布公告信息",notes="发布公告信息",response = Response.class)
	    @RequestMapping(value = "add_notice", method = RequestMethod.POST)
	    public Response publishNotice(@ModelAttribute()Notice notice) {
	        Response Response = new Response();
	        Subject currentUser = SecurityUtils.getSubject();
	        Session session = currentUser.getSession(false);
	        //判断是否登陆
	        if(null != session) {
	           
	                OMSRealm.ShiroOmsUser principal = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
	                if(null != principal){
	                	notice.setCreateId(Long.valueOf(principal.getId().toString()));
	                	noticeService.publishNotice(notice);
	                }else{
	                    throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
	                }
	            
	        }else{
	            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
	        }
	        return Response;
	    }
	    /**
	     * 查询最新10公告
	     * @author gmli
	     * @since 2016.5.26 
	     */
	    @ApiOperation(value="平台最新公告查询",notes="平台最新公告查询",response = Response.class)
	    @RequestMapping(value = "sel_platform_newnotice", method = RequestMethod.GET)
	    public Response queryPlatformNewNotice() {
	    	 Response Response = new Response();
	          //查询
	          List<Notice> exhibitionList = noticeService.queryPlatformNewNotice();
	          Response.setData(exhibitionList);
	          return Response;
	    }
}
