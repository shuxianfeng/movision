package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.coupon.CouponFacade;
import com.movision.mybatis.couponDistributeManage.entity.CouponDistributeManage;
import com.movision.mybatis.couponDistributeManage.entity.CouponDistributeManageVo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/2/24 20:44
 */
@RestController
@RequestMapping("/app/coupon/")
public class AppCouponController {

    @Autowired
    private CouponFacade couponFacade;

    @ApiOperation(value = "查询当前可领优惠券列表", notes = "用户在商品详情页点击可领优惠券返回所有可领取的优惠券列表", response = Response.class)
    @RequestMapping(value = "curReceiveCouponList", method = RequestMethod.POST)
    public Response queryCurReceiveCoupon(@ApiParam(value = "是否为三元自营：1是(自营) 0否(三方)") @RequestParam String isself,
                                          @ApiParam(value = "店铺id（isself为0 三方店铺时不为空）") @RequestParam(required = false) String shopid) {
        Response response = new Response();

        List<CouponDistributeManageVo> couponDistributeManageList = couponFacade.queryCurReceiveCoupon(isself, shopid);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(couponDistributeManageList);
        return response;
    }
}
