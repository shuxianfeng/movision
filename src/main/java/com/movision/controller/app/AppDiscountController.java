package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.discount.DiscountFacade;
import com.movision.mybatis.goodsDiscount.entity.GoodsDiscountVo;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/2/28 11:29
 */
@RestController
@RequestMapping("/app/discount/")
public class AppDiscountController {

    @Autowired
    private DiscountFacade discountFacade;

    @ApiOperation(value = "查询商品所有优惠活动列表", notes = "用户在商品详情页点击活动公告返回商品所有优惠活动列表（status 0 未开始 1 进行中 2 已结束）", response = Response.class)
    @RequestMapping(value = "queryGoodsDiscountList", method = RequestMethod.POST)
    public Response queryGoodsDiscountList() {
        Response response = new Response();

        List<GoodsDiscountVo> goodsDiscountList = discountFacade.querygoodsDiscount();

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(goodsDiscountList);
        return response;
    }
}
