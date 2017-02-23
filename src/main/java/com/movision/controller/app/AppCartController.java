package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.cart.CartFacade;
import com.movision.mybatis.cart.entity.CartVo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

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
                                 @ApiParam(value = "套餐id") @RequestParam(required = false) String comboid,
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

    /**
     * 查询用户购物车中的商品列表
     */
    @ApiOperation(value = "查询用户购物车中的商品列表", notes = "用户点击购物车图标或者点击去结算，跳转到购物车时返回购物车中所有商品列表", response = Response.class)
    @RequestMapping(value = "queryCartByUser", method = RequestMethod.POST)
    public Response queryCartByUser(@ApiParam(value = "用户id") @RequestParam String userid) {
        Response response = new Response();

        List<CartVo> cartList = cartFacade.queryCartByUser(userid);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(cartList);
        return response;
    }

    /**
     * 购物车删除接口（批量删除和单个删除）
     */
    @ApiOperation(value = "购物车删除接口（批量删除和单个删除）", notes = "用于用户购物车中的商品批量删除和单个删除的接口", response = Response.class)
    @RequestMapping(value = "deleteCartGoods", method = RequestMethod.POST)
    public Response deleteCartGoods(@ApiParam(value = "用户id") @RequestParam String userid,
                                    @ApiParam(value = "购物车id字符串（多个用英文逗号隔开）") @RequestParam String cartids) {
        Response response = new Response();

        cartFacade.deleteCartGoods(userid, cartids);

        if (response.getCode() == 200) {
            response.setMessage("删除成功");
        }
        return response;
    }

    /**
     * 购物车商品————修改商品数量接口
     */
    @ApiOperation(value = "购物车商品————修改商品数量接口", notes = "用于用户在购物车中直接对单个商品的数量进行修改", response = Response.class)
    @RequestMapping(value = "updateCartGoodsSum", method = RequestMethod.POST)
    public Response updateCartGoodsSum(@ApiParam(value = "购物车id") @RequestParam String cartid,
                                       @ApiParam(value = "修改类型：0 减 1 加") @RequestParam String type
    ) {
        Response response = new Response();

        int sum = cartFacade.updateCartGoodsSum(cartid, type);//返回修改后的商品数量

        if (response.getCode() == 200) {
            response.setMessage("修改成功");
        }
        response.setData(sum);
        return response;
    }
}
