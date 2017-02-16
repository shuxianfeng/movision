package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.Goods.GoodsFacade;
import com.movision.mybatis.goods.entity.GoodsDetail;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessment;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentVo;
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

    /**
     * 商品评论接口
     */
    @ApiOperation(value = "商品评论接口", notes = "用户点击商品缩略图进入商品详情页面，详情数据返回接口", response = Response.class)
    @RequestMapping(value = "goodAssessment", method = RequestMethod.POST)
    public Response queryGoodAssessment(@ApiParam(value = "商品id") @RequestParam String goodsid,
                                        @ApiParam(value = "评论类型(0全部 1有图 2质量好 3送货快 4态度不错 5质量一般)") @RequestParam String type,
                                        @ApiParam(value = "第几页") @RequestParam(required = false) String pageNo,
                                        @ApiParam(value = "每页几条") @RequestParam(required = false) String pageSize) {
        Response response = new Response();

        Map<String, Object> map = goodsFacade.queryGoodsAssessment(pageNo, pageSize, goodsid, type);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }
}
