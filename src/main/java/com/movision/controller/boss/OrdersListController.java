package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.OrderFacade;
import com.movision.mybatis.address.entity.Address;
import com.movision.mybatis.bossOrders.entity.BossOrders;
import com.movision.mybatis.bossOrders.entity.BossOrdersVo;
import com.movision.mybatis.invoice.entity.Invoice;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.bcel.verifier.VerifierAppFrame;
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
        Map<String, Object> list = orderFacade.queryOrderList(pageNo, pageSize);//获取订单列表
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(list);
        return response;
    }


    /**
     * 删除订单
     * @param id
     * @return
     */
    @ApiOperation(value = "删除订单",notes = "删除订单",response = Response.class)
    @RequestMapping(value = "delete_order",method = RequestMethod.POST)
    public  Response deleteOrder(@RequestParam(required = false) Integer id){
        Response response = new Response();
        int result = orderFacade.deleteOrder(id);
        if(response.getCode()==200){
            response.setMessage("删除成功");
        }
        response.setData(result);
        return  response;
    }

    /**
     * 根据条件快速查询订单
     *
     * @param ordernumber
     * @param name
     * @param status
     * @param
     * @return
     */
    @ApiOperation(value = "按条件查询订单(不填写搜索全部)", notes = "按条件查询订单", response = Response.class)
    @RequestMapping(value = "/query_order_condition", method = RequestMethod.POST)
    public Response QueryuickConditionByOrder(@ApiParam(value = "订单编号") @RequestParam(required = false) String ordernumber,
                                              @ApiParam(value = "收货人姓名") @RequestParam(required = false) String name,
                                              @ApiParam(value = "订单状态（0待付款1待发货2待收货4待评价）") @RequestParam(required = false) String status,
                                              @ApiParam(value = "订单类型（0 租赁 1 购买）") @RequestParam(required = false) String position,
                                              @ApiParam(value = "物流单号") @RequestParam(required = false) String logisticid,
                                              @ApiParam(value = "订单最大时间") @RequestParam(required = false) String mintime,
                                              @ApiParam(value = "订单最小时间") @RequestParam(required = false) String maxtime,
                                              @RequestParam(required = false) String pageNo,
                                              @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        Paging<BossOrdersVo> pager = new Paging<BossOrdersVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<BossOrdersVo> list = orderFacade.queryOrderByCondition(ordernumber, name, status, position, logisticid, mintime, maxtime, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 编辑收货人地址
     * @param orderid
     * @param phone
     * @param name
     * @param email
     * @param province
     * @param city
     * @param district
     * @param street
     * @return
     */
    @ApiOperation(value = "编辑收货人地址", notes = "编辑收货人地址", response = Response.class)
    @RequestMapping(value = "/update_address", method = RequestMethod.POST)
    public Response updateOrderAddress(@ApiParam(value = "订单id") @RequestParam(required = false) String orderid,
                                       @ApiParam(value = "电话") @RequestParam(required = false) String phone,
                                       @ApiParam(value = "收货人姓名") @RequestParam(required = false) String name,
                                       @ApiParam(value = "邮箱") @RequestParam(required = false) String email,
                                       @ApiParam(value = "省") @RequestParam(required = false) String province,
                                       @ApiParam(value = "市") @RequestParam(required = false) String city,
                                       @ApiParam(value = "区") @RequestParam(required = false) String district,
                                       @ApiParam(value = "街道") @RequestParam(required = false) String street) {
        Response response = new Response();
        Map<String, Integer> map = orderFacade.updateOrderAddress(orderid, phone, name, email, province, city, district, street);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
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

    /**
     * 订单管理--修改发票
     *
     * @param
     * @return
     */
    @ApiOperation(value = "修改发票", notes = "修改发票", response = Response.class)
    @RequestMapping(value = "/update_order_Invoice", method = RequestMethod.POST)
    public Response updateOrderInvoice(
            @ApiParam(value = "订单id") @RequestParam(required = false) String orderid,
            @ApiParam(value = "发票抬头") @RequestParam(required = false) String head,
            @ApiParam(value = "发票类型") @RequestParam(required = false) String kind,
            @ApiParam(value = "发票内容") @RequestParam(required = false) String content,
            @ApiParam(value = "单位名称") @RequestParam(required = false) String companyname,
            @ApiParam(value = "注册地址") @RequestParam(required = false) String rigaddress,
            @ApiParam(value = "注册电话") @RequestParam(required = false) String rigphone,
            @ApiParam(value = "开户银行") @RequestParam(required = false) String bank,
            @ApiParam(value = "银行账户") @RequestParam(required = false) String banknum,
            @ApiParam(value = "纳税人识别码") @RequestParam(required = false) String code) {
        Response response = new Response();
        Map<String, Integer> map = orderFacade.updateOrderInvoice(head, kind, content, orderid, companyname, rigaddress, rigphone, bank, banknum, code);
        if (response.getCode() == 200) {
            response.setMessage("编辑发票");
        }
        response.setData(map);
        return response;
    }

    /**
     * 订单管理--返回发票
     *
     * @param orderid
     * @return
     */
    @ApiOperation(value = "返回发票", notes = "返回发票", response = Response.class)
    @RequestMapping(value = "/query_invoice", method = RequestMethod.POST)
    public Response queryOrderInvoice(@ApiParam(value = "订单id") @RequestParam(required = false) Integer orderid) {
        Response response = new Response();
        Invoice invoice = orderFacade.queryOrderInvoice(orderid);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(invoice);
        return response;

    }


    /**
     * 订单管理--查询地址
     *
     * @param orderid
     * @return
     */
    @ApiOperation(value = "查询地址", notes = "查询地址", response = Response.class)
    @RequestMapping(value = "query_address", method = RequestMethod.POST)
    public Response queryOrderAddress(@ApiParam(value = "订单id") @RequestParam(required = false) Integer orderid) {
        Response response = new Response();
        List<Address> list = orderFacade.queryOrderAddress(orderid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(list);
        return response;
    }

    /**
     * 订单管理--订单详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "订单详情", notes = "订单详情", response = Response.class)
    @RequestMapping(value = "query_orderdetail", method = RequestMethod.POST)
    public Response queryOrderGoods(@ApiParam(value = "订单id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        Map<String, Object> map = orderFacade.queryOrderDetail(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }
}
