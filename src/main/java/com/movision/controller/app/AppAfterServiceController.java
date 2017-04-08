package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.afterService.AfterServiceFacade;
import com.movision.mybatis.afterservice.entity.AfterServiceVo;
import com.movision.mybatis.logisticsCompany.entity.LogisticsCompany;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/4/6 11:18
 */
@RestController
@RequestMapping("/app/afterService/")
public class AppAfterServiceController {

    @Autowired
    private AfterServiceFacade afterServiceFacade;

    @ApiOperation(value = "APP申请售后接口", notes = "用于用户在APP对订单中的商品申请售后的接口", response = Response.class)
    @RequestMapping(value = "commitAfterService", method = RequestMethod.POST)
    public Response commitAfterService(@ApiParam(value = "用户id") @RequestParam String userid,
                                       @ApiParam(value = "主订单id") @RequestParam String orderid,
                                       @ApiParam(value = "订单收货地址id") @RequestParam String addressid,
                                       @ApiParam(value = "商品id") @RequestParam String goodsid,
                                       @ApiParam(value = "售后类型（1退货2退款3换货）") @RequestParam String afterstatue,
                                       @ApiParam(value = "申请退款金额") @RequestParam(required = false) String amountdue,
                                       @ApiParam(value = "问题描述") @RequestParam String remark,
                                       @ApiParam(value = "描述图1（最多3张）") @RequestParam(value = "file1", required = false) MultipartFile imgfile1,
                                       @ApiParam(value = "描述图2（最多3张）") @RequestParam(value = "file2", required = false) MultipartFile imgfile2,
                                       @ApiParam(value = "描述图3（最多3张）") @RequestParam(value = "file3", required = false) MultipartFile imgfile3) {
        Response response = new Response();

        Map<String, Object> map = afterServiceFacade.commitAfterService(userid, orderid, addressid, goodsid, afterstatue, amountdue, remark, imgfile1, imgfile2, imgfile3);

        if (response.getCode() == 200 && (int) map.get("code") == 200) {
            response.setMessage("申请成功");
            response.setData(map);
        } else if ((int) map.get("code") == 300) {
            response.setCode(300);
            response.setMessage("重复申请");
        } else {
            response.setCode(400);
            response.setMessage("申请失败");
        }
        return response;
    }

    @ApiOperation(value = "APP取消售后申请接口", notes = "用于用户在APP对已申请售后的商品取消申请操作的接口", response = Response.class)
    @RequestMapping(value = "cancelAfterService", method = RequestMethod.POST)
    public Response cancelAfterService(@ApiParam(value = "用户id") @RequestParam String userid,
                                       @ApiParam(value = "售后单号afterserviceid") @RequestParam String afterserviceid) {
        Response response = new Response();

        int count = afterServiceFacade.cancelAfterService(userid, afterserviceid);

        if (response.getCode() == 200 && count == 1) {
            response.setMessage("取消成功");
        } else {
            response.setCode(300);
            response.setMessage("售后申请不存在或无操作权限,取消失败");
        }
        return response;
    }

    @ApiOperation(value = "APP售后单详情接口", notes = "用于用户在APP对已申请售后的商品取消申请操作的接口", response = Response.class)
    @RequestMapping(value = "queryAfterServiceDetail", method = RequestMethod.POST)
    public Response queryAfterServiceDetail(@ApiParam(value = "售后单号afterserviceid") @RequestParam String afterserviceid) {
        Response response = new Response();

        Map<String, Object> map = afterServiceFacade.queryAfterServiceDetail(afterserviceid);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
            response.setData(map);
        } else {
            response.setCode(300);
            response.setMessage("查询失败");
        }
        return response;
    }

    @ApiOperation(value = "APP售后下拉选择商品退回的物流类别接口", notes = "用于用户在APP对已受理的售后单下拉选择物流列表枚举值的接口", response = Response.class)
    @RequestMapping(value = "queryLogisticType", method = RequestMethod.POST)
    public Response queryLogisticType() {
        Response response = new Response();

        List<LogisticsCompany> logisticsCompanyList = afterServiceFacade.queryLogisticType();

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
            response.setData(logisticsCompanyList);
        } else {
            response.setCode(300);
            response.setMessage("查询失败");
        }
        return response;
    }

    @ApiOperation(value = "APP提交商品退回的物流信息的接口", notes = "APP提交商品退回的物流信息的接口(含物流类型和物流单号)", response = Response.class)
    @RequestMapping(value = "commitReturnLogisticInfo", method = RequestMethod.POST)
    public Response commitReturnLogisticInfo(@ApiParam(value = "售后单id") @RequestParam String id,
                                             @ApiParam(value = "物流类别id") @RequestParam String logisticid,
                                             @ApiParam(value = "商品退回物流单号") @RequestParam String returnnumber) {
        Response response = new Response();

        afterServiceFacade.commitReturnLogisticInfo(id, logisticid, returnnumber);

        if (response.getCode() == 200) {
            response.setMessage("提交成功");
        } else {
            response.setCode(300);
            response.setMessage("提交失败");
        }
        return response;
    }
}
