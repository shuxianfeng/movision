package com.zhuhuibao.mobile.web.mc;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.zhuhuibao.service.MobileOrderService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 订单controller
 * 
 * @author tongxinglong
 * @date 2016/10/18 0018.
 */
@RestController
@RequestMapping("/rest/m/order/mc")
public class MobileOrderController {

    @Autowired
    private MobileOrderService mobileOrderService;

    @RequestMapping(value = "/sel_order_list", method = RequestMethod.GET)
    @ApiOperation(value = "我的订单列表", notes = "我的订单列表", response = Response.class)
    public Response selOrderList(@ApiParam(value = "订单状态：1未支付，2：已支付，3：退款中，4，退款失败，5：已退款 , 6:已失效") @RequestParam(required = false) String status,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
            @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if (memberId != null) {
            Paging<Map<String, String>> orderPager = mobileOrderService.getOrderList(memberId, status, pageNo, pageSize);
            response.setData(orderPager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @RequestMapping(value = "/sel_order_detail", method = RequestMethod.GET)
    @ApiOperation(value = "查看订单详情", notes = "查看订单详情", response = Response.class)
    public Response selectTechDataDetail(@ApiParam(value = "订单编号") @RequestParam String orderNo) {

        Long memberId = ShiroUtil.getCreateID();
        if (memberId == null) {
            throw new AuthException(MsgCodeConstant.un_login, "请登录");
        }
        Map<String, Object> orderDetail = mobileOrderService.getOrderDetail(orderNo, memberId);

        return new Response(orderDetail);
    }

    @ApiOperation(value = "收银台页面信息", notes = "收银台页面信息", response = Response.class)
    @RequestMapping(value = "/cashier/sel_cashierDesk", method = RequestMethod.GET)
    public Response selectCashierDeskInfo(@ApiParam(value = "订单编号") @RequestParam String orderNo) {

        Map<String, Object> cashierDesk = mobileOrderService.getCashierDeskInfo(orderNo);
        Response response = new Response();
        response.setData(cashierDesk);
        return response;
    }
}
