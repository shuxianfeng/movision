package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.cart.CartFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

/**
 * @Author shuxf
 * @Date 2017/2/18 17:12
 * 用于提供所有的购物车相关接口
 */
@RestController
@RequestMapping("/app/cart/")
public class AppCartController {

    @Autowired
    private CartFacade cartFacade;

    /**
     * 商品加入购物车接口（租用的和出售的共用）
     */
    @ApiOperation(value = "商品加入购物车接口（租用的和出售的共用）", notes = "商品被加入购物车", response = Response.class)
    @RequestMapping(value = "addGoodsCart", method = RequestMethod.POST)
    public Response addGoodsCart(@ApiParam(value = "用户id") @RequestParam String userid,
                                 @ApiParam(value = "商品id") @RequestParam String goodsid,
                                 @ApiParam(value = "套餐id") @RequestParam String comboid,
                                 @ApiParam(value = "是否需要跟机员（0 不需要 1 需要）") @RequestParam String isdebug,
                                 @ApiParam(value = "数量") @RequestParam String sum,
                                 @ApiParam(value = "商品定位：0 租赁 1 出售") @RequestParam String type,
                                 @ApiParam(value = "租赁日期（租赁商品时为必填。多天使用年-月-日，并用英文逗号隔开）") @RequestParam(required = false) String rentdate) throws ParseException {
        Response response = new Response();

        int count = cartFacade.addGoodsCart(userid, goodsid, comboid, isdebug, sum, type, rentdate);

        if (count == 1) {
            response.setCode(200);
            response.setMessage("加入成功");
        } else if (count == 0) {
            response.setCode(300);
            response.setMessage("加入失败");
        }
        return response;
    }
}
