package com.zhuhuibao.business.oms.platform;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.business.oms.expert.ExpertOmsController;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.oms.entity.ComplainSuggest;
import com.zhuhuibao.mybatis.oms.service.ComplainSuggestService;
import com.zhuhuibao.shiro.realm.OMSRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gmli on 2016/5/26.
 * 处理用户投诉意见
 * 
 */
@RestController
@RequestMapping("/rest/complain/oms")
@Api(value="PlatForm")
public class ComplainSuggestController {
    private static final Logger log = LoggerFactory.getLogger(ExpertOmsController.class);

    @Autowired
    private ComplainSuggestService complainSuggestService;
    
    /**
     * 投诉建议管理
     * @author gmli
     * @since 2016.5.26 
     */
    @ApiOperation(value="用户建议查询",notes="用户建议查询",response = Response.class)
    @RequestMapping(value = "sel_complaint_suggest", method = RequestMethod.POST)
    public Response queryComplaintSuggest(@ApiParam(value = "标题")@RequestParam(required = false) String title,
            @ApiParam(value = "状态")@RequestParam(required = false)String status,
            @ApiParam(value = "联系人")@RequestParam(required = false) String userName,
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
          Paging<ComplainSuggest> pager = new Paging<ComplainSuggest>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
          Map<String,Object> map = new HashMap<>();
          //查询传参
          map.put("title",title);
          map.put("status",status);
          map.put("userName",userName);
          //查询
          List<ComplainSuggest> exhibitionList = complainSuggestService.findAllComplaintSuggest(pager,map);
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
    @RequestMapping(value = "upd_complain_suggest", method = RequestMethod.POST)
    public Response updateComplainSuggest(@ModelAttribute()ComplainSuggest complainSuggest)  {
        Response Response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session) {
            OMSRealm.ShiroOmsUser principal = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
            if(null != principal){
                
            	complainSuggestService.updateComplainSuggest(complainSuggest);
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
    @RequestMapping(value = "sel_complain_suggestById", method = RequestMethod.GET)
    public Response queryComplainSuggestById(@RequestParam Long id)  {
        Response Response = new Response();
        ComplainSuggest meetingOrder = complainSuggestService.queryComplainSuggestById(id);
        Response.setData(meetingOrder);
        return Response;
    }

}
