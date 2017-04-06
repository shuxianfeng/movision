package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.afterService.AfterServiceFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

        afterServiceFacade.commitAfterService(userid, orderid, addressid, goodsid, afterstatue, amountdue, remark, imgfile1, imgfile2, imgfile3);

        if (response.getCode() == 200) {
            response.setMessage("申请成功");
        } else {
            response.setCode(300);
            response.setMessage("申请失败");
        }
        return response;
    }
}
