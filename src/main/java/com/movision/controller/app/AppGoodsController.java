package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.Goods.GoodsFacade;
import com.movision.mybatis.goods.entity.GoodsDetail;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author shuxf
 * @Date 2017/2/15 18:17
 * 用于提供商品相关的所有接口
 */
@RestController
@RequestMapping("/app/goods/")
public class AppGoodsController {

    @Autowired
    private GoodsFacade goodsFacade;

    /**
     * 商品详情接口
     */
    @ApiOperation(value = "点击商品缩略图进入商品详情页面", notes = "用户点击商品缩略图返回商品详情数据接口", response = Response.class)
    @RequestMapping(value = "goodDetail", method = RequestMethod.POST)
    public Response queryGoodDetail(@ApiParam(value = "商品id") @RequestParam String goodsid) {
        Response response = new Response();

        GoodsDetail goodsDetail = goodsFacade.queryGoodDetail(goodsid);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(goodsDetail);
        return response;
    }
}
