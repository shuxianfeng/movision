package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.OrderFacade;
import com.movision.mybatis.bossOrders.entity.BossOrders;
import com.movision.mybatis.bossOrders.entity.BossOrdersVo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/2/6 10:17
 */
@RestController
@RequestMapping("/boos/order")
public class OrdersListController {
    @Autowired
    OrderFacade orderFacade = new OrderFacade();

    /**
     * 查询订单列表接口
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询订单列表（分页）", notes = "查询订单列表（分页）", response = Response.class)
    @RequestMapping(value = "/query_order_list", method = RequestMethod.POST)
    public Response QueryOrderList(@RequestParam(required = false) String pageNo,//required默认是true表示必须
                                   @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        List<BossOrdersVo> list = orderFacade.queryOrderList(pageNo, pageSize);//获取订单列表
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(list);
        return response;
    }

    /**
     * 根据条件查询订单
     *
     * @param ordernumber
     * @param name
     * @param status
     * @param takeway
     * @return
     */
    @ApiOperation(value = "按条件查询订单", notes = "按条件查询订单", response = Response.class)
    @RequestMapping(value = "/query_order_condition", method = RequestMethod.POST)
    public Response QueryConditionByOrder(@ApiParam(value = "订单编号") @RequestParam(required = false) String ordernumber,
                                          @ApiParam(value = "收货人姓名") @RequestParam(required = false) String name,
                                          @ApiParam(value = "订单状态（0待付款1待发货2待收货4待评价）") @RequestParam(required = false) String status,
                                          @ApiParam(value = "订单类型（取货方式0自取1送货上门）") @RequestParam(required = false) String takeway) {
        Response response = new Response();
        List<BossOrdersVo> list = orderFacade.queryOrderByCondition(ordernumber, name, status, takeway);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(list);
        return response;
    }
}
