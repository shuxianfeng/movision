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
import java.util.Map;

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
     * 查询基本信息
     * @param  id
     * @param
     * @return
     */
    @ApiOperation(value ="查询基本信息",notes = "查询基本信息",response = Response.class)
    @RequestMapping(value = "/query_info",method = RequestMethod.POST)
    public Response queryOrderInfo(@RequestParam(required = false) Integer id){
        Response response = new Response();
        BossOrders list = orderFacade.queryOrderInfo(id);
        if(response.getCode()==200){
            response.setMessage("查询成功");
        }
        response.setData(list);
        return  response;
    }

    /**
     * 根据条件快速查询订单
     *
     * @param ordernumber
     * @param name
     * @param status
     * @param takeway
     * @return
     */
    @ApiOperation(value = "按条件查询订单(不填写搜索全部)", notes = "按条件查询订单", response = Response.class)
    @RequestMapping(value = "/query_order_condition", method = RequestMethod.POST)
    public Response QueryuickConditionByOrder(@ApiParam(value = "订单编号") @RequestParam(required = false) String ordernumber,
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

    /**
     * 精确查询订单
     *
     * @param ordernumber
      * @param province
     * @param city
     * @param district
     * @param takeway
     * @param mintime
     * @param maxtime
     * @param email
     * @param name
     * @param phone
     * @param paytype
     * @return
     */
    @ApiOperation(value = "精准搜索订单", notes = "精准搜索订单", response = Response.class)
    @RequestMapping(value = "/query_order_accuracy", method = RequestMethod.POST)
    public Response queryAccuracyConditionByOrder(@ApiParam(value = "订单编号") @RequestParam(required = false) String ordernumber,//订单号
                                                   @ApiParam(value = "省") @RequestParam(required = false) String province,//省
                                                  @ApiParam(value = "市") @RequestParam(required = false) String city,//市
                                                  @ApiParam(value = "县") @RequestParam(required = false) String district,//县
                                                  @ApiParam(value = "配送方式") @RequestParam(required = false) String takeway,//配送方式
                                                  @ApiParam(value = "区间最小时间") @RequestParam(required = false) String mintime,//区间最小时间
                                                  @ApiParam(value = "区间最大时间") @RequestParam(required = false) String maxtime,//区间最大时间
                                                  @ApiParam(value = "邮箱") @RequestParam(required = false) String email,//
                                                  @ApiParam(value = "收货人") @RequestParam(required = false) String name,//收货人
                                                  @ApiParam(value = "手机号") @RequestParam(required = false) String phone,//手机号
                                                  @ApiParam(value = "支付方式") @RequestParam(required = false) String paytype//支付方式
    ) {
        Response response = new Response();
        List<BossOrdersVo> list = orderFacade.queryAccuracyConditionByOrder(ordernumber
                 , province, city, district, takeway, mintime,
                maxtime, email, name, phone, paytype);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(list);
        return response;
    }

    /**
     * 订单基本信息
     *
     * @param ordernumber
     * @return
     */
    @ApiOperation(value = "订单基本信息", notes = "订单基本信息", response = Response.class)
    @RequestMapping(value = "/query_order_essential_information", method = RequestMethod.POST)
    public Response queryOrderParticulars(@ApiParam(value = "订单编号") @RequestParam String ordernumber) {
        Response response = new Response();
        BossOrdersVo bossOrders = orderFacade.queryOrderParticulars(ordernumber);
        if (response.getCode() == 200) {
            response.setMessage("查询查询成功");
        }
        response.setData(bossOrders);
        return response;
    }

    /**
     * 三级联动
     *
     * @return
     */
    @ApiOperation(value = "三级联动", notes = "三级联动", response = Response.class)
    @RequestMapping(value = "/query_list_province_type", method = RequestMethod.POST)
    public Response queryPostProvince() {
        Response response = new Response();
        Map<String, Object> list = orderFacade.queryPostProvince();
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(list);
        return response;
    }
}
