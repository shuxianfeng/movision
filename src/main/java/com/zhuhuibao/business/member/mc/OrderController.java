package com.zhuhuibao.business.member.mc;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.order.entity.Order;
import com.zhuhuibao.mybatis.tech.service.OrderManagerService;
import com.zhuhuibao.service.course.CourseService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的订单管理 全部订单
 * @author pl
 * @version 2016/6/12 0012
 */
@RestController
@RequestMapping("/rest/member/mc/order")
@Api(value = "orderMc", description = "我的订单管理 全部订单")
public class OrderController {
    @Autowired
    OrderManagerService orderService;

    CourseService courseService;

    @RequestMapping(value="sel_orderlist", method = RequestMethod.GET)
    @ApiOperation(value="我的订单管理",notes = "我的订单管理",response = Response.class)
    public Response findAllTechDataPager(@ApiParam(value = "订单状态：1未支付，2：已支付，3：退款中，4，退款失败，5：已退款 , 6:已失效") @RequestParam(required = false) String status,
                                         @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
                                         @ApiParam(value = "每页显示的数目") @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        Long buyerId = ShiroUtil.getCreateID();
        if(buyerId != null) {
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("status", status);
            condition.put("buyerId",buyerId);
            if (StringUtils.isEmpty(pageNo)) {
                pageNo = "1";
            }
            if (StringUtils.isEmpty(pageSize)) {
                pageSize = "10";
            }
            Paging<Map<String, String>> pager = new Paging<Map<String, String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            List<Map<String, String>> orderList = orderService.findAllOmsTechOrder(pager, condition);
            pager.result(orderList);
            response.setData(pager);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @RequestMapping(value="sel_order_detail", method = RequestMethod.GET)
    @ApiOperation(value="查看订单详情",notes = "查看订单详情",response = Response.class)
    public Response selectTechDataDetail(@ApiParam(value = "订单编号")  @RequestParam String orderNo)
    {
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("orderNo",orderNo);
        Map<String,Object> orderList = orderService.selectOrderDetail(condition);
        Response response = new Response();
        response.setData(orderList);
        return response;
    }

    @RequestMapping(value="upd_sendSNCode", method = RequestMethod.GET)
    @ApiOperation(value="再次发送SN码",notes = "再次发送SN码",response = Response.class)
    public Response sendSNCode(@ApiParam(value = "订单编号")  @RequestParam String orderNo) throws Exception {
        Order order = new Order();
        order.setOrderNo(orderNo);
        List<Order> orderList = new ArrayList<Order>();
        orderList.add(order);
        courseService.sendSMS(orderList, PropertiesUtils.getValue("course_begin_sms_template_code"));
        Response response = new Response();
        response.setData(orderList);
        return response;
    }

}
