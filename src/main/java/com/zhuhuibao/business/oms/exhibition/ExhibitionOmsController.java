package com.zhuhuibao.business.oms.exhibition;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.mybatis.memCenter.entity.DistributedOrder;
import com.zhuhuibao.mybatis.memCenter.entity.Exhibition;
import com.zhuhuibao.mybatis.memCenter.entity.MeetingOrder;
import com.zhuhuibao.mybatis.memCenter.service.ExhibitionService;
import com.zhuhuibao.shiro.realm.OMSRealm;
import com.zhuhuibao.shiro.realm.ShiroRealm;
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
 * 会展接口类
 * Created by cxx on 2016/5/11 0011.
 */
@RestController
@RequestMapping("/rest/oms")
public class ExhibitionOmsController {
    private static final Logger log = LoggerFactory.getLogger(ExhibitionOmsController.class);

    @Autowired
    private ExhibitionService exhibitionService;

    /**
     * 一站式会展定制申请处理
     */
    @ApiOperation(value="会展定制申请处理",notes="会展定制申请处理",response = Response.class)
    @RequestMapping(value = "updateMeetingOrderStatus", method = RequestMethod.POST)
    public Response updateMeetingOrderStatus(@ModelAttribute()MeetingOrder meetingOrder)  {
        Response Response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session) {
            OMSRealm.ShiroOmsUser principal = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
            if(null != principal){
                meetingOrder.setUpdateManId(principal.getId().toString());
                exhibitionService.updateMeetingOrderStatus(meetingOrder);
            }else{
                Response.setCode(401);
                Response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                Response.setMsgCode(MsgCodeConstant.un_login);
            }
        }else{
            Response.setCode(401);
            Response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            Response.setMsgCode(MsgCodeConstant.un_login);
        }
        return Response;
    }

    /**
     * 一站式会展定制查看
     */
    @ApiOperation(value="会展定制查看",notes="会展定制查看",response = Response.class)
    @RequestMapping(value = "queryMeetingOrderInfoById", method = RequestMethod.GET)
    public Response queryMeetingOrderInfoById(@RequestParam String id)  {
        Response Response = new Response();
        MeetingOrder meetingOrder = exhibitionService.queryMeetingOrderInfoById(id);
        Response.setData(meetingOrder);
        return Response;
    }

    /**
     * 一站式会展定制列表
     */
    @ApiOperation(value="一站式会展定制列表",notes="一站式会展定制列表",response = Response.class)
    @RequestMapping(value = "findAllMeetingOrderInfo", method = RequestMethod.GET)
    public Response findAllMeetingOrderInfo(
            @ApiParam(value = "账号")@RequestParam(required = false) String account,
            @ApiParam(value = "省")@RequestParam(required = false)String province,
            @ApiParam(value = "市")@RequestParam(required = false)String city,
            @ApiParam(value = "审核状态")@RequestParam(required = false)String status,
            @RequestParam(required = false)String pageNo,@RequestParam(required = false)String pageSize)  {
        Response Response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<MeetingOrder> pager = new Paging<MeetingOrder>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        //查询传参
        map.put("account",account);
        map.put("province",province);
        map.put("city",city);
        map.put("status",status);
        //查询
        List<MeetingOrder> meetingOrderList = exhibitionService.findAllMeetingOrderInfo(pager,map);
        pager.result(meetingOrderList);
        Response.setData(pager);
        return Response;
    }

    /**
     * 发布会展信息
     */
    @ApiOperation(value="发布会展信息",notes="发布会展信息",response = Response.class)
    @RequestMapping(value = "publishExhibition", method = RequestMethod.POST)
    public Response publishExhibition(@ModelAttribute()Exhibition exhibition) {
        Response Response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        //判断是否登陆
        if(null != session) {
            if("1".equals(exhibition.getCreaterType())) {
                OMSRealm.ShiroOmsUser principal = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
                if(null != principal){
                    exhibition.setCreateid(principal.getId().toString());
                    exhibitionService.publishExhibition(exhibition);
                }else{
                    Response.setCode(401);
                    Response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                    Response.setMsgCode(MsgCodeConstant.un_login);
                }
            }else{
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
                if(null != principal){
                    exhibition.setCreateid(principal.getId().toString());
                    exhibitionService.publishExhibition(exhibition);
                }else{
                    Response.setCode(401);
                    Response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                    Response.setMsgCode(MsgCodeConstant.un_login);
                }
            }
        }else{
            Response.setCode(401);
            Response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            Response.setMsgCode(MsgCodeConstant.un_login);
        }
        return Response;
    }

    /**
     * 会展信息列表
     */
    @ApiOperation(value="会展信息列表(运营)",notes="会展信息列表(运营)",response = Response.class)
    @RequestMapping(value = "findAllExhibitionOms", method = RequestMethod.GET)
    public Response findAllExhibitionOms(@ApiParam(value = "标题")@RequestParam(required = false)String title,
                                           @ApiParam(value = "所属栏目")@RequestParam(required = false)String type,
                                           @ApiParam(value = "审核状态")@RequestParam(required = false)String status,
                                           @ApiParam(value = "状态")@RequestParam(required = false)String type2,
        @RequestParam(required = false)String pageNo,@RequestParam(required = false)String pageSize) {
        Response Response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Exhibition> pager = new Paging<Exhibition>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        //查询传参
        map.put("title",title);
        map.put("type",type);
        map.put("status",status);
        map.put("type2",type2);
        //查询
        List<Exhibition> exhibitionList = exhibitionService.findAllExhibition(pager,map);
        pager.result(exhibitionList);
        Response.setData(pager);
        return Response;
    }

    /**
     * 分布式会展定制列表
     */
    @ApiOperation(value="分布式会展定制列表(运营)",notes="分布式会展定制列表(运营)",response = Response.class)
    @RequestMapping(value = "findAllDistributedOrder", method = RequestMethod.GET)
    public Response findAllDistributedOrder(@ApiParam(value = "联系手机")@RequestParam(required = false)String mobile,
                                           @ApiParam(value = "定制类型")@RequestParam(required = false)String type,
                                           @ApiParam(value = "审核状态")@RequestParam(required = false)String status,
                                           @RequestParam(required = false)String pageNo,@RequestParam(required = false)String pageSize) {
        Response Response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<DistributedOrder> pager = new Paging<DistributedOrder>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        //查询传参
        map.put("mobile",mobile);
        map.put("type",type);
        map.put("status",status);
        //查询
        List<DistributedOrder> distributedOrderList = exhibitionService.findAllDistributedOrder(pager,map);
        pager.result(distributedOrderList);
        Response.setData(pager);
        return Response;
    }

    /**
     * 分布式会展定制查看
     */
    @ApiOperation(value="分布式会展定制查看",notes="分布式会展定制查看",response = Response.class)
    @RequestMapping(value = "queryDistributedOrderInfoById", method = RequestMethod.GET)
    public Response queryDistributedOrderInfoById(@RequestParam String id)  {
        Response Response = new Response();
        DistributedOrder distributedOrder = exhibitionService.queryDistributedOrderInfoById(id);
        Response.setData(distributedOrder);
        return Response;
    }


    /**
     * 分布式会展定制申请处理
     */
    @ApiOperation(value="分布式会展定制申请处理",notes="分布式会展定制申请处理",response = Response.class)
    @RequestMapping(value = "updateDistributedStatus", method = RequestMethod.POST)
    public Response updateDistributedStatus(@ModelAttribute()DistributedOrder distributedOrder)  {
        Response Response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session) {
            OMSRealm.ShiroOmsUser principal = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
            if(null != principal){
                distributedOrder.setUpdateManId(principal.getId().toString());
                exhibitionService.updateDistributedStatus(distributedOrder);
            }else{
                Response.setCode(401);
                Response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                Response.setMsgCode(MsgCodeConstant.un_login);
            }
        }else{
            Response.setCode(401);
            Response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            Response.setMsgCode(MsgCodeConstant.un_login);
        }
        return Response;
    }
}
