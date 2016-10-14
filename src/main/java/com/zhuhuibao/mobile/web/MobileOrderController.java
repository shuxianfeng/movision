package com.zhuhuibao.mobile.web;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.tech.service.OrderManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * ${description}
 *
 * @author pl
 * @version 2016/6/23 0023
 */
@RestController("cashier")
@RequestMapping(value = "/rest/m/order/site")
@Api(value = "OrderController", description = "下单流程")
public class MobileOrderController {

    @Autowired
    OrderManagerService orderManagerService;

    @ApiOperation(value = "收银台页面信息", notes = "收银台页面信息", response = Response.class)
    @RequestMapping(value = "cashier/sel_cashierDesk", method = RequestMethod.GET)
    public Response selectCashierDeskInfo(@ApiParam(value = "订单编号") @RequestParam String orderNo) {
    	
        Map<String, Object> cashierDesk = orderManagerService.selectCashierDeskInfo(orderNo);
        Response response = new Response();
        response.setData(cashierDesk);
        return response;
    }

}
