package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.SystemSettingFacade;
import com.movision.mybatis.logisticsfeeCalculateRule.entity.LogisticsfeeCalculateRule;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统设置
 *
 * @Author zhurui
 * @Date 2017/3/6 21:00
 */
@RestController
@RequestMapping(value = "/boss/system")
public class SystemSettingController {

    @Autowired
    private SystemSettingFacade systemSettingFacade;

    /**
     * 运费计算规则
     *
     * @param shopid
     * @param startprice
     * @param startdistance
     * @param beyondbilling
     * @return
     */
    @ApiOperation(value = "运费计算规则", notes = "用于运费计算接口", response = Response.class)
    @RequestMapping(value = "update_carriage_calculate", method = RequestMethod.POST)
    public Response updateCarriageCalculate(@ApiParam(value = "店铺id") @RequestParam String shopid,
                                            @ApiParam(value = "起步价") @RequestParam String startprice,
                                            @ApiParam(value = "起步公里数") @RequestParam String startdistance,
                                            @ApiParam(value = "超出计费") @RequestParam String beyondbilling,
                                            @ApiParam(value = "运费封顶") @RequestParam String capping) {
        Response response = new Response();
        int i = systemSettingFacade.updateCarriageCalculate(shopid, startprice, startdistance, beyondbilling, capping);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(i);
        return response;
    }

    /**
     * 查询运算计费规则
     *
     * @param shopid
     * @return
     */
    @ApiOperation(value = "查询运费计算规则", notes = "用于查询运费计算规则接口", response = Response.class)
    @RequestMapping(value = "query_carriiage_calculate", method = RequestMethod.POST)
    public Response queryCarriageCalculate(@ApiParam(value = "店铺id") @RequestParam String shopid) {
        Response response = new Response();
        LogisticsfeeCalculateRule logisticsfeeCalculateRule = systemSettingFacade.queryCarriageCalculate(shopid);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(logisticsfeeCalculateRule);
        return response;
    }
}
