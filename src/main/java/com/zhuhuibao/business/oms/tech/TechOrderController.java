package com.zhuhuibao.business.oms.tech;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.tech.service.OrderManagerService;
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
 * 订单管理
 *
 * @author Administrator
 * @version 2016/6/6 0006
 */
@RestController
@RequestMapping
@Api(value = "techOrder", description = "技术培训订单管理接口")
public class TechOrderController {
    @Autowired
    OrderManagerService orderService;

    @RequestMapping(value={"/rest/tech/oms/order/sel_order","/rest/expert/oms/order/sel_order"}, method = RequestMethod.GET)
    @ApiOperation(value="运营管理平台技术培训订单管理",notes = "运营管理平台技术培训订单管理",response = Response.class)
    public Response findAllTechDataPager(@ApiParam(value = "课程名称") @RequestParam(required = false) String goodsName,
                                         @ApiParam(value = "订单状态：1未支付，2：已支付，3：退款中，4，退款失败，5：已退款 , 6:已失效") @RequestParam(required = false) String status,
                                         @ApiParam(value = "购买者名称") @RequestParam(required = false) String buyerName,
                                         @ApiParam(value = "商品类型 :1：技术培训，2：专家培训 3：VIP服务套餐订单") String goodsType,
                                         @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
                                         @ApiParam(value = "每页显示的数目") @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("status", status);
        condition.put("goodsType", goodsType);
        if(goodsName != null && !goodsName.equals(""))
        {
            condition.put("goodsName",goodsName.replaceAll("_","\\_"));
        }
        if(buyerName != null && !buyerName.equals(""))
        {
            condition.put("buyerName",buyerName.replaceAll("_","\\_"));
        }
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String, String>> pager = new Paging<Map<String, String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        condition.put("status", status);
        List<Map<String,String>> orderList = orderService.findAllOmsTechOrder(pager, condition);
        pager.result(orderList);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value={"/rest/tech/oms/order/sel_order_detail","/rest/expert/oms/order/sel_order_detail"}, method = RequestMethod.GET)
    @ApiOperation(value="查看订单详情",notes = "查看订单详情",response = Response.class)
    public Response selectTechDataDetail(@ApiParam(value = "订单编号")  @RequestParam String orderNo,
                                         @ApiParam(value = "商品类型 :1：技术培训，2：专家培训 3：VIP服务套餐订单")  @RequestParam Long goodsType)
    {
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("orderNo",orderNo);
        condition.put("goodsType",goodsType);
        Map<String,Object> orderList = orderService.selectOrderDetail(condition);
        Response response = new Response();
        response.setData(orderList);
        return response;
    }

    @RequestMapping(value={"/rest/tech/oms/order/upd_order_status","/rest/expert/oms/order/upd_order_status"}, method = RequestMethod.GET)
    @ApiOperation(value="更新订单详情",notes = "更新订单详情",response = Response.class)
    public Response updateTechDataDetail(@ApiParam(value = "订单编号")  @RequestParam String orderNo,
                                         @ApiParam(value = "状态")  @RequestParam String status)
    {
        int result = orderService.updateByPrimaryKeySelective(orderNo,status);
        Response response = new Response();
        return response;
    }
}
