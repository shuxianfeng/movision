package com.zhuhuibao.business.job.site;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.JobConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.EmployeeSize;
import com.zhuhuibao.mybatis.memCenter.entity.EnterpriseType;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.mybatis.memCenter.service.JobRelResumeService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.mybatis.payment.service.PaymentGoodsService;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@RequestMapping("/rest/job/site/base/")
public class JobCommonController {
    private final static Logger log = LoggerFactory.getLogger(JobCommonController.class);

    @Autowired
    JobPositionService job;

    @Autowired
    ResumeService resume;

    @Autowired
    SiteMailService smService;

    @Autowired
    JobRelResumeService jrrService;

    @Autowired
    ChannelNewsService newsService;

    @Autowired
    PaymentGoodsService goodsService;

    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "sel_unRead_msg_count", method = RequestMethod.GET)
    @ApiOperation(value = "未读消息数目", notes = "人才网未读消息数目", response = Response.class)
    public Response queryUnreadMsgCount() {
        Response response = new Response();
        Long receiveID = ShiroUtil.getCreateID();
        if (receiveID != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("recID", receiveID);
            map.put("type", JobConstant.SITEMAIL_TYPE_JOB_ELEVEN);
            map.put("status", Constants.MAILSITE_STATUS_UNREAD);
            response.setData(smService.queryUnreadMsgCount(map));
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "企业性质", notes = "企业性质", response = Response.class)
    @RequestMapping(value = "sel_enterpriseTypeList", method = RequestMethod.GET)
    public Response enterpriseTypeList() {
        Response result = new Response();
        List<EnterpriseType> enterpriseType = memberService.findEnterpriseTypeList();
        result.setData(enterpriseType);
        return result;
    }

    @ApiOperation(value = "人员规模", notes = "人员规模", response = Response.class)
    @RequestMapping(value = "sel_employeeSizeList", method = RequestMethod.GET)
    public Response employeeSizeList() {
        Response result = new Response();
        List<EmployeeSize> employeeSizeList = memberService.findEmployeeSizeList();
        result.setData(employeeSizeList);
        return result;
    }

    @ApiOperation(value = "人才网资讯列表", notes = "人才网资讯列表", response = Response.class)
    @RequestMapping(value = "sel_newslist", method = RequestMethod.GET)
    public Response listNews(@ApiParam("资讯类别 14:面试技巧 15:职场动态 16:行业资讯") @RequestParam String type,
                             @ApiParam("限制条数") @RequestParam(required = false) String count,
                             @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                             @ApiParam(value = "每页显示的条数") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Paging<Map<String,String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> params = new HashMap<>();
        params.put("channelid", type);
        params.put("status", "1");   //已审核
        if(!StringUtils.isEmpty(count)){
            params.put("count",Integer.valueOf(count));
        }
        List<Map<String,String>> list = newsService.findAllPassNewsByType(pager, params);
        pager.result(list);
        return new Response(pager);
    }

    @ApiOperation(value = "人才网资讯详情", notes = "人才网资讯详情", response = Response.class)
    @RequestMapping(value = "sel_newsdetail", method = RequestMethod.GET)
    public Response newsDetail(@ApiParam("资讯ID") @RequestParam String id){

        Response response = newsService.selectByPrimaryKey(Long.valueOf(id));
        newsService.updateViews(Long.valueOf(id));
        return response;
    }

    @ApiOperation(value = "人才网资讯详情|列表>热门排行", notes = "人才网资讯详情|列表>热门排行", response = Response.class)
    @RequestMapping(value = "sel_hotviews", method = RequestMethod.GET)
    public Response hotViews(@ApiParam("人才网:13")@RequestParam String type,
                             @ApiParam("记录条数") @RequestParam(required = false,defaultValue = "10") String count){

        List<Map<String,String>>  list = newsService.selectHotViews(type,Integer.valueOf(count));
        return new Response(list);
    }

    @ApiOperation(value = "人才网资讯列表>最新资讯", notes = "人才网资讯列表>最新排行", response = Response.class)
    @RequestMapping(value = "sel_topnews", method = RequestMethod.GET)
    public Response topNews(@ApiParam("人才网:13")@RequestParam String type,
                            @ApiParam("记录条数") @RequestParam(required = false,defaultValue = "10") String count){

        List<Map<String,String>>  list = newsService.selectNewViews(type,Integer.valueOf(count));
        return new Response(list);
    }


    @ApiOperation(value = "人才网首页>最新资讯", notes = "人才网首页>最新排行", response = Response.class)
    @RequestMapping(value = "sel_lastestnews", method = RequestMethod.GET)
    public Response latestNews(@ApiParam(value = "记录条数" ,defaultValue = "5") String count){

        Map<String,Object> map = newsService.selectLastestNews(count);
        return new Response(map);
    }

    @ApiOperation(value = "获取职位类别", notes = "获取职位类别", response = Response.class)
    @RequestMapping(value = "sel_positionType", method = RequestMethod.GET)
    public Response positionType() {
        Response response = new Response();
        List list = job.positionType();
        response.setData(list);
        return response;
    }
}
