package com.zhuhuibao.business.oms.tech;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.tech.entity.TrainPwdTicket;
import com.zhuhuibao.mybatis.tech.service.TrainPwdTicketManage;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 培训密码券业务处理类
 *
 * @author Administrator
 * @version 2016/6/6 0006
 */
@RestController
@RequestMapping("/rest/tech/oms/ticket")
@Api(value = "trainPwdTicket", description = "培训密码券")
public class TrainPwdTicketController {
    @Autowired
    TrainPwdTicketManage ticketManage;

    @RequestMapping(value="sel_pwd_ticket", method = RequestMethod.GET)
    @ApiOperation(value="运营管理平台查询培训密码券",notes = "运营管理平台查询培训密码券",response = Response.class)
    public Response findAllTechDataPager(@ApiParam(value = "SN码") @RequestParam(required = false) String snCode,
                                         @ApiParam(value = "SN码状态:1有效，2已使用，3已过期，4已取消，5未生效") @RequestParam(required = false) String status,
                                         @ApiParam(value = "手机号码") @RequestParam(required = false) String mobile,
                                         @ApiParam(value = "类型：1：技术培训 2：专家培训") @RequestParam String type,
                                         @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
                                         @ApiParam(value = "每页显示的数目") @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("status", status);
        condition.put("type", type);
        if(snCode != null && !snCode.equals(""))
        {
            condition.put("snCode",snCode.replaceAll("_","\\_"));
        }
        if(mobile != null && !mobile.equals(""))
        {
            condition.put("mobile",mobile.replaceAll("_","\\_"));
        }
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String, String>> pager = new Paging<Map<String, String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        condition.put("status", status);
        List<Map<String,String>> orderList = ticketManage.findAllTrainPwdTicket(pager, condition);
        pager.result(orderList);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value="upd_pwd_ticket_status", method = RequestMethod.POST)
    @ApiOperation(value="运营管理平台更新培训密码券",notes = "运营管理平台更新培训密码券",response = Response.class)
    public Response updateTrainPwdTicketStatus(@ApiParam(value = "密码券ID") @RequestParam Long ticketId,
                                               @ApiParam(value = "状态：1有效，2已使用，3已过期，4已取消，5未生效") @RequestParam String status)
    {
        Response response = new Response();
        int result = ticketManage.updateTrainPwdTicketStatus(ticketId,status);
        return response;
    }

    @RequestMapping(value="sel_pwd_ticket_detail", method = RequestMethod.GET)
    @ApiOperation(value="运营管理平台查看培训密码券",notes = "运营管理平台查看培训密码券",response = Response.class)
    public Response selectTrainPwdTicket(@ApiParam(value = "密码券ID") @RequestParam Long ticketId)
    {
        Response response = new Response();
        Map<String,Object> ticket = ticketManage.selectTrainPwdTicket(ticketId);
        response.setData(ticket);
        return response;
    }
}
