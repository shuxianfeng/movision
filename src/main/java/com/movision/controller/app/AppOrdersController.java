package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.address.AddressFacade;
import com.movision.facade.order.OrderAppFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/3/3 20:41
 */
@RestController
@RequestMapping("/app/orders/")
public class AppOrdersController {

    @Autowired
    private OrderAppFacade orderAppFacade;

    @Autowired
    private AddressFacade addressFacade;

    @ApiOperation(value = "确认订单页面（点击提交订单前）更换可用地址时动态调用", notes = "根据前台App用户选择的定位地点和用户选择的可用收货地址计算用户购物车中运费总和", response = Response.class)
    @RequestMapping(value = "calculateLogisticsfee", method = RequestMethod.POST)
    public Response calculateLogisticsfee(@ApiParam(value = "购物车id(多件商品的购物车id用英文逗号','隔开)") @RequestParam String cartids,
                                          @ApiParam(value = "省code(收货地址选择的可用收货地址的省code)") @RequestParam String provincecode,
                                          @ApiParam(value = "市code((收货地址选择的可用收货地址的市code)") @RequestParam String citycode,
                                          @ApiParam(value = "当前选择的定位地址-省code") @RequestParam String positionprovincecode,
                                          @ApiParam(value = "当前选择的定位地址-市code") @RequestParam String positioncitycode,
                                          @ApiParam(value = "经度(选择的可用收货地址的经度)") @RequestParam String lng,
                                          @ApiParam(value = "纬度(选择的可用收货地址的纬度)") @RequestParam String lat) {
        Response response = new Response();

        Map<String, Object> feemap = addressFacade.calculateLogisticsfee(cartids, provincecode, citycode, positionprovincecode, positioncitycode, lng, lat);

        if (response.getCode() == 200) {
            response.setMessage("计算成功");
            response.setData(feemap);
        } else {
            response.setMessage("计算失败");
        }
        return response;
    }

    @ApiOperation(value = "APP提交订单接口", notes = "用于用户提交订单的接口", response = Response.class)
    @RequestMapping(value = "commitOrder", method = RequestMethod.POST)
    public Response commitOrder(@ApiParam(value = "用户id") @RequestParam String userid,
                                @ApiParam(value = "地址id") @RequestParam String addressid,
                                @ApiParam(value = "购物车id") @RequestParam String cartids,

                                @ApiParam(value = "开票种类：1 普通发票 2 增值税发票") @RequestParam String kind,
                                @ApiParam(value = "状态：1 个人 2企业（普通发票时为必填）") @RequestParam(required = false) String onlystatue,
                                @ApiParam(value = "发票抬头（个人时取姓名 企业和增值税时取企业名称）（普通发票时为必填）") @RequestParam(required = false) String head,
                                @ApiParam(value = "开票内容") @RequestParam String content,
                                @ApiParam(value = "发票邮寄地址id") @RequestParam String invoiceaddressid,
                                @ApiParam(value = "企业名称(增值税发票时为必填)") @RequestParam(required = false) String companyname,
                                @ApiParam(value = "注册地址(增值税发票时为必填)") @RequestParam(required = false) String rigaddress,
                                @ApiParam(value = "注册电话(增值税发票时为必填)") @RequestParam(required = false) String rigphone,
                                @ApiParam(value = "开户银行(增值税发票时为必填)") @RequestParam(required = false) String bank,
                                @ApiParam(value = "银行账户（卡号）(增值税发票时为必填)") @RequestParam(required = false) String banknum,
                                @ApiParam(value = "纳税识别码(增值税发票时为必填)") @RequestParam(required = false) String code,

                                @ApiParam(value = "配送方式（0 自提 1 快递（送货上门））") @RequestParam String takeway,
                                @ApiParam(value = "优惠券id(选填)") @RequestParam(required = false) String couponid,
                                @ApiParam(value = "使用积分数(选填)") @RequestParam(required = false) String points,
                                @ApiParam(value = "买家留言（订单备注,选填）") @RequestParam(required = false) String message,
                                @ApiParam(value = "快递费") @RequestParam String logisticsfee,
                                @ApiParam(value = "订单总额") @RequestParam String totalprice,
                                @ApiParam(value = "实付款（实付款=订单总额-优惠券金额-积分优惠金额+运费总额）") @RequestParam String payprice
    ) {
        Response response = new Response();

        Map<String, Object> map = orderAppFacade.commitOrder(userid, addressid, cartids, takeway, kind, onlystatue, head, content, invoiceaddressid,
                companyname, rigaddress, rigphone, bank, banknum, code, couponid, points, message, logisticsfee, totalprice, payprice);
        if ((int) map.get("code") == 200) {
            response.setCode(200);
            response.setMessage("订单提交成功");
            response.setData(map);
        } else {
            response.setCode(300);
            response.setMessage("订单提交失败");
            response.setData(map);
        }
        return response;
    }

    @ApiOperation(value = "APP订单详情接口", notes = "用于用户在APP查询订单详情的接口", response = Response.class)
    @RequestMapping(value = "queryOrderDetail", method = RequestMethod.POST)
    public Response queryOrderDetail(@ApiParam(value = "订单id") @RequestParam String orderid) {
        Response response = new Response();

        Map<String, Object> map = orderAppFacade.queryOrderDetail(orderid);

        if (response.getCode() == 200) {
            response.setMessage("订单详情查询成功");
            response.setData(map);
        } else {
            response.setCode(300);
            response.setMessage("订单详情查询失败");
        }
        return response;
    }
}
